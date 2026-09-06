package co.jinear.core.manager.oauth.provider;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.oauth.OauthConsentInfoDto;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.request.oauth.OauthConsentRequest;
import co.jinear.core.model.response.oauth.OauthConsentInfoResponse;
import co.jinear.core.model.response.oauth.OauthConsentResponse;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.oauth.provider.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAuthorizationManager {

    private static final String RESPONSE_TYPE_CODE = "code";

    private final OauthClientService oauthClientService;
    private final RedirectUriMatcher redirectUriMatcher;
    private final PkceValidator pkceValidator;
    private final OauthScopeService oauthScopeService;
    private final OauthAuthorizationRequestService oauthAuthorizationRequestService;
    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthConnectionService oauthConnectionService;
    private final SessionInfoService sessionInfoService;
    private final OauthProperties oauthProperties;
    private final McpProperties mcpProperties;
    private final FeProperties feProperties;
    private final InstanceFlagService instanceFlagService;

    public String authorize(OauthAuthorizeRequestVo vo) {
        assertEnabled();

        OauthClientMetadataVo client = oauthClientService.resolveForAuthorization(vo.getClientId());
        List<String> registeredRedirects = oauthClientService.redirectUrisOf(client);
        if (!redirectUriMatcher.matchesAny(registeredRedirects, vo.getRedirectUri())) {
            log.warn("[OAUTH] Redirect uri is not registered. clientId: {}, redirectUri: {}", vo.getClientId(), vo.getRedirectUri());
            throw new BusinessException("oauth.error.invalid-redirect-uri");
        }

        if (!RESPONSE_TYPE_CODE.equals(vo.getResponseType())) {
            return errorRedirect(vo, "unsupported_response_type", "Only the authorization code flow is supported.");
        }
        if (Objects.isNull(vo.getCodeChallenge()) || vo.getCodeChallenge().isBlank()) {
            return errorRedirect(vo, "invalid_request", "A PKCE code challenge is required.");
        }
        if (!pkceValidator.isSupportedMethod(vo.getCodeChallengeMethod())) {
            return errorRedirect(vo, "invalid_request", "Only the S256 code challenge method is supported.");
        }
        if (!isResourceAcceptable(vo.getResource())) {
            return errorRedirect(vo, "invalid_target", "The requested resource does not match this server.");
        }

        Set<String> scopes = oauthScopeService.parse(vo.getScope());
        if (scopes.isEmpty()) {
            scopes = oauthScopeService.defaultScopes();
        }

        OauthAuthorizationRequest request = oauthAuthorizationRequestService.initialize(
                vo.getClientId(),
                vo.getRedirectUri(),
                scopes,
                vo.getState(),
                vo.getCodeChallenge(),
                vo.getCodeChallengeMethod(),
                vo.getResource());

        log.info("[OAUTH] Parked authorization request. requestId: {}, clientId: {}",
                request.getOauthAuthorizationRequestId(), vo.getClientId());
        return feProperties.getOauthConsentUrl().replace("{requestId}", request.getOauthAuthorizationRequestId());
    }

    public OauthConsentInfoResponse retrieveConsentInfo(String requestId) {
        assertEnabled();
        OauthAuthorizationRequest request = oauthAuthorizationRequestService.retrievePending(requestId);
        OauthClientMetadataVo client = oauthClientService.resolveForAuthorization(request.getClientId());
        List<String> registeredRedirects = oauthClientService.redirectUrisOf(client);

        OauthConsentInfoDto dto = new OauthConsentInfoDto();
        dto.setRequestId(requestId);
        dto.setClientDisplayHost(displayHost(request.getClientId()));
        dto.setClientName(client.getClientName());
        dto.setClientUri(client.getClientUri());
        dto.setLogoUri(client.getLogoUri());
        dto.setPolicyUri(client.getPolicyUri());
        dto.setTosUri(client.getTosUri());
        dto.setRedirectHost(hostOfRedirect(request.getRedirectUri()));
        dto.setLoopbackOnly(redirectUriMatcher.allLoopback(registeredRedirects));
        dto.setRequestedScopes(List.copyOf(oauthScopeService.parse(request.getScope())));

        OauthConsentInfoResponse response = new OauthConsentInfoResponse();
        response.setOauthConsentInfoDto(dto);
        return response;
    }

    public OauthConsentResponse submitConsent(OauthConsentRequest consentRequest) {
        assertEnabled();
        String accountId = sessionInfoService.currentAccountId();
        OauthAuthorizationRequest request = oauthAuthorizationRequestService.retrievePending(consentRequest.getRequestId());

        OauthConsentResponse response = new OauthConsentResponse();
        if (!Boolean.TRUE.equals(consentRequest.getApproved())) {
            oauthAuthorizationRequestService.complete(request);
            log.info("[OAUTH] Consent denied. requestId: {}, accountId: {}", request.getOauthAuthorizationRequestId(), accountId);
            response.setRedirectUrl(buildRedirect(request.getRedirectUri(), Map.of(
                    "error", "access_denied",
                    "error_description", "The user declined the connection."), request.getState()));
            return response;
        }

        Set<String> scopes = oauthScopeService.parse(request.getScope());
        String clientName = oauthClientService.displayNameFor(request.getClientId());
        OauthConnection connection = oauthConnectionService.grant(accountId, request.getClientId(), clientName, scopes);
        String code = oauthAuthorizationCodeService.issue(request, accountId, connection.getOauthConnectionId());
        oauthAuthorizationRequestService.complete(request);

        log.info("[OAUTH] Consent granted. requestId: {}, accountId: {}, connectionId: {}",
                request.getOauthAuthorizationRequestId(), accountId, connection.getOauthConnectionId());
        response.setRedirectUrl(buildRedirect(request.getRedirectUri(), Map.of("code", code), request.getState()));
        return response;
    }

    private void assertEnabled() {
        if (!Boolean.TRUE.equals(oauthProperties.getEnabled())
                || !instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)) {
            throw new BusinessException("oauth.error.disabled");
        }
    }

    private boolean isResourceAcceptable(String resource) {
        if (Objects.isNull(resource) || resource.isBlank()) {
            return true;
        }
        return normalize(resource).equals(normalize(mcpProperties.getResourceUrl()));
    }

    private String normalize(String uri) {
        String trimmed = uri.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }

    private String displayHost(String clientId) {
        String host = oauthClientService.hostOf(clientId);
        return Objects.isNull(host) ? clientId : host;
    }

    private String hostOfRedirect(String redirectUri) {
        try {
            String host = URI.create(redirectUri).getHost();
            return Objects.isNull(host) ? redirectUri : host;
        } catch (IllegalArgumentException exception) {
            return redirectUri;
        }
    }

    private String errorRedirect(OauthAuthorizeRequestVo vo, String error, String description) {
        log.warn("[OAUTH] Authorization refused. clientId: {}, error: {}", vo.getClientId(), error);
        return buildRedirect(vo.getRedirectUri(), Map.of("error", error, "error_description", description), vo.getState());
    }

    private String buildRedirect(String redirectUri, Map<String, String> params, String state) {
        Map<String, String> all = new LinkedHashMap<>(params);
        if (Objects.nonNull(state) && !state.isBlank()) {
            all.put("state", state);
        }
        StringBuilder builder = new StringBuilder(redirectUri);
        builder.append(redirectUri.contains("?") ? "&" : "?");
        boolean first = true;
        for (Map.Entry<String, String> entry : all.entrySet()) {
            if (!first) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            first = false;
        }
        return builder.toString();
    }
}
