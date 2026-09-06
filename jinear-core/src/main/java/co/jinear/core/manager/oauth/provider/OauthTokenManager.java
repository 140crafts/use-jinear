package co.jinear.core.manager.oauth.provider;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthAuthorizationCode;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.entity.oauth.OauthRefreshToken;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.service.oauth.provider.*;
import co.jinear.core.system.oauth.OauthTokenHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthTokenManager {

    private static final String GRANT_AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_REFRESH_TOKEN = "refresh_token";

    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthRefreshTokenService oauthRefreshTokenService;
    private final OauthConnectionService oauthConnectionService;
    private final OauthClientService oauthClientService;
    private final PkceValidator pkceValidator;
    private final OauthScopeService oauthScopeService;
    private final OauthTokenHelper oauthTokenHelper;
    private final OauthProperties oauthProperties;
    private final McpProperties mcpProperties;

    public Map<String, Object> token(Map<String, String> form) {
        assertEnabled();
        String grantType = form.get("grant_type");
        if (GRANT_AUTHORIZATION_CODE.equals(grantType)) {
            return exchangeAuthorizationCode(form);
        }
        if (GRANT_REFRESH_TOKEN.equals(grantType)) {
            return refresh(form);
        }
        throw new BusinessException("oauth.error.invalid-grant");
    }

    public Map<String, Object> register(OauthClientMetadataVo request) {
        assertEnabled();
        OauthClientMetadataVo registered = oauthClientService.registerDynamicClient(request);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("client_id", registered.getClientId());
        body.put("client_id_issued_at", System.currentTimeMillis() / 1000);
        body.put("client_name", registered.getClientName());
        body.put("redirect_uris", registered.getRedirectUris());
        body.put("grant_types", registered.getGrantTypes());
        body.put("response_types", List.of("code"));
        body.put("token_endpoint_auth_method", "none");
        return body;
    }

    public void revoke(String token) {
        assertEnabled();
        if (Objects.isNull(token) || token.isBlank()) {
            return;
        }
        try {
            OauthRefreshToken refreshToken = oauthRefreshTokenService.redeem(token);
            oauthRefreshTokenService.revokeAllForConnection(refreshToken.getOauthConnectionId());
            oauthConnectionService.revoke(refreshToken.getOauthConnectionId());
        } catch (RuntimeException exception) {
            log.info("[OAUTH] Revocation presented an unusable token, answering 200 anyway.");
        }
    }

    private Map<String, Object> exchangeAuthorizationCode(Map<String, String> form) {
        OauthAuthorizationCode code = oauthAuthorizationCodeService.redeem(form.get("code"));

        String clientId = form.get("client_id");
        if (Objects.nonNull(clientId) && !clientId.equals(code.getClientId())) {
            log.warn("[OAUTH] client_id at the token endpoint does not match the code. codeClient: {}", code.getClientId());
            throw new BusinessException("oauth.error.invalid-grant");
        }
        if (!Objects.equals(code.getRedirectUri(), form.get("redirect_uri"))) {
            log.warn("[OAUTH] redirect_uri at the token endpoint does not match the authorization request.");
            throw new BusinessException("oauth.error.invalid-grant");
        }
        if (!pkceValidator.verify(form.get("code_verifier"), code.getCodeChallenge())) {
            log.warn("[OAUTH] PKCE verification failed. clientId: {}", code.getClientId());
            throw new BusinessException("oauth.error.invalid-grant");
        }
        assertResourceMatches(form.get("resource"));

        OauthConnection connection = oauthConnectionService.retrieveOptional(code.getOauthConnectionId())
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-grant"));

        Set<String> scopes = oauthScopeService.parse(code.getScope());
        return buildTokenResponse(connection, scopes);
    }

    private Map<String, Object> refresh(Map<String, String> form) {
        OauthRefreshToken refreshToken = oauthRefreshTokenService.redeem(form.get("refresh_token"));
        OauthConnection connection = oauthConnectionService.retrieveOptional(refreshToken.getOauthConnectionId())
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-grant"));
        assertResourceMatches(form.get("resource"));

        Set<String> granted = oauthScopeService.parse(connection.getGrantedScopes());
        Set<String> requested = oauthScopeService.parse(form.get("scope"));
        Set<String> effective = requested.isEmpty() ? granted : requested;
        if (!oauthScopeService.grants(granted, effective)) {
            throw new BusinessException("oauth.error.invalid-grant");
        }

        String rotated = oauthRefreshTokenService.rotate(refreshToken);
        return buildTokenResponse(connection, effective, rotated);
    }

    private Map<String, Object> buildTokenResponse(OauthConnection connection, Set<String> scopes) {
        String refreshToken = scopes.contains(OauthScope.OFFLINE_ACCESS.getValue())
                ? oauthRefreshTokenService.issue(connection.getOauthConnectionId())
                : null;
        return buildTokenResponse(connection, scopes, refreshToken);
    }

    private Map<String, Object> buildTokenResponse(OauthConnection connection, Set<String> scopes, String refreshToken) {
        Date expiresAt = DateHelper.addMinutes(DateHelper.now(), oauthProperties.getAccessTokenValidityMinutes());
        String accessToken = oauthTokenHelper.generateAccessToken(
                connection.getAccountId(),
                connection.getOauthConnectionId(),
                connection.getClientId(),
                scopes,
                expiresAt);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("access_token", accessToken);
        body.put("token_type", "Bearer");
        body.put("expires_in", oauthProperties.getAccessTokenValidityMinutes() * 60L);
        body.put("scope", oauthScopeService.format(scopes));
        if (Objects.nonNull(refreshToken)) {
            body.put("refresh_token", refreshToken);
        }
        return body;
    }

    private void assertResourceMatches(String resource) {
        if (Objects.isNull(resource) || resource.isBlank()) {
            return;
        }
        if (!normalize(resource).equals(normalize(mcpProperties.getResourceUrl()))) {
            log.warn("[OAUTH] Token requested for another resource: {}", resource);
            throw new BusinessException("oauth.error.invalid-grant");
        }
    }

    private String normalize(String uri) {
        String trimmed = uri.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }

    private void assertEnabled() {
        if (!Boolean.TRUE.equals(oauthProperties.getEnabled())) {
            throw new BusinessException("oauth.error.disabled");
        }
    }
}
