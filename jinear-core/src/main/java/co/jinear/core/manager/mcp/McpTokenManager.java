package co.jinear.core.manager.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpAuthorizationCode;
import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.entity.mcp.McpRefreshToken;
import co.jinear.core.model.enumtype.mcp.McpScope;
import co.jinear.core.model.vo.mcp.McpClientMetadataVo;
import co.jinear.core.service.mcp.oauth.*;
import co.jinear.core.system.mcp.McpTokenHelper;
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

/**
 * Token, registration and revocation endpoints.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpTokenManager {

    private static final String GRANT_AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_REFRESH_TOKEN = "refresh_token";

    private final McpAuthorizationCodeService mcpAuthorizationCodeService;
    private final McpRefreshTokenService mcpRefreshTokenService;
    private final McpConnectionService mcpConnectionService;
    private final McpOauthClientService mcpOauthClientService;
    private final McpPkceValidator mcpPkceValidator;
    private final McpScopeService mcpScopeService;
    private final McpTokenHelper mcpTokenHelper;
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
        throw new BusinessException("mcp.error.oauth.invalid-grant");
    }

    public Map<String, Object> register(McpClientMetadataVo request) {
        assertEnabled();
        McpClientMetadataVo registered = mcpOauthClientService.registerDynamicClient(request);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("client_id", registered.getClientId());
        body.put("client_id_issued_at", System.currentTimeMillis() / 1000);
        body.put("client_name", registered.getClientName());
        body.put("redirect_uris", registered.getRedirectUris());
        body.put("grant_types", registered.getGrantTypes());
        body.put("response_types", List.of("code"));
        // Public client: no secret is issued, PKCE is what protects the exchange.
        body.put("token_endpoint_auth_method", "none");
        return body;
    }

    /**
     * RFC 7009. Revoking a refresh token ends the whole connection, which is what a
     * user disconnecting from their client expects. The RFC requires a 200 whether or
     * not the token was valid, so unknown tokens are swallowed.
     */
    public void revoke(String token) {
        assertEnabled();
        if (Objects.isNull(token) || token.isBlank()) {
            return;
        }
        try {
            McpRefreshToken refreshToken = mcpRefreshTokenService.redeem(token);
            mcpRefreshTokenService.revokeAllForConnection(refreshToken.getMcpConnectionId());
            mcpConnectionService.revoke(refreshToken.getMcpConnectionId());
        } catch (RuntimeException exception) {
            log.info("[MCP] Revocation presented an unusable token, answering 200 anyway.");
        }
    }

    private Map<String, Object> exchangeAuthorizationCode(Map<String, String> form) {
        McpAuthorizationCode code = mcpAuthorizationCodeService.redeem(form.get("code"));

        String clientId = form.get("client_id");
        if (Objects.nonNull(clientId) && !clientId.equals(code.getClientId())) {
            log.warn("[MCP] client_id at the token endpoint does not match the code. codeClient: {}", code.getClientId());
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        if (!Objects.equals(code.getRedirectUri(), form.get("redirect_uri"))) {
            log.warn("[MCP] redirect_uri at the token endpoint does not match the authorization request.");
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        if (!mcpPkceValidator.verify(form.get("code_verifier"), code.getCodeChallenge())) {
            log.warn("[MCP] PKCE verification failed. clientId: {}", code.getClientId());
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        assertResourceMatches(form.get("resource"));

        McpConnection connection = mcpConnectionService.retrieveOptional(code.getMcpConnectionId())
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.invalid-grant"));

        Set<String> scopes = mcpScopeService.parse(code.getScope());
        return buildTokenResponse(connection, scopes);
    }

    private Map<String, Object> refresh(Map<String, String> form) {
        McpRefreshToken refreshToken = mcpRefreshTokenService.redeem(form.get("refresh_token"));
        McpConnection connection = mcpConnectionService.retrieveOptional(refreshToken.getMcpConnectionId())
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.invalid-grant"));
        assertResourceMatches(form.get("resource"));

        Set<String> granted = mcpScopeService.parse(connection.getGrantedScopes());
        // A refresh may narrow the scope set but never widen it.
        Set<String> requested = mcpScopeService.parse(form.get("scope"));
        Set<String> effective = requested.isEmpty() ? granted : requested;
        if (!mcpScopeService.grants(granted, effective)) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }

        String rotated = mcpRefreshTokenService.rotate(refreshToken);
        return buildTokenResponse(connection, effective, rotated);
    }

    private Map<String, Object> buildTokenResponse(McpConnection connection, Set<String> scopes) {
        String refreshToken = scopes.contains(McpScope.OFFLINE_ACCESS.getValue())
                ? mcpRefreshTokenService.issue(connection.getMcpConnectionId())
                : null;
        return buildTokenResponse(connection, scopes, refreshToken);
    }

    private Map<String, Object> buildTokenResponse(McpConnection connection, Set<String> scopes, String refreshToken) {
        Date expiresAt = DateHelper.addMinutes(DateHelper.now(), mcpProperties.getAccessTokenValidityMinutes());
        String accessToken = mcpTokenHelper.generateAccessToken(
                connection.getAccountId(),
                connection.getMcpConnectionId(),
                connection.getClientId(),
                scopes,
                expiresAt);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("access_token", accessToken);
        body.put("token_type", "Bearer");
        body.put("expires_in", mcpProperties.getAccessTokenValidityMinutes() * 60L);
        body.put("scope", mcpScopeService.format(scopes));
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
            log.warn("[MCP] Token requested for another resource: {}", resource);
            throw new BusinessException("mcp.error.oauth.invalid-grant");
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
        if (!Boolean.TRUE.equals(mcpProperties.getEnabled())) {
            throw new BusinessException("mcp.error.disabled");
        }
    }
}
