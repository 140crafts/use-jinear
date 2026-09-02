package co.jinear.core.manager.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.mcp.McpConsentInfoDto;
import co.jinear.core.model.entity.mcp.McpAuthorizationRequest;
import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.request.mcp.McpConsentRequest;
import co.jinear.core.model.response.mcp.McpConsentInfoResponse;
import co.jinear.core.model.response.mcp.McpConsentResponse;
import co.jinear.core.model.vo.mcp.McpAuthorizeRequestVo;
import co.jinear.core.model.vo.mcp.McpClientMetadataVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.mcp.oauth.*;
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

/**
 * The authorization endpoint and the consent exchange behind it.
 * <p>
 * The endpoint never renders anything itself. It validates, parks the request, and
 * redirects to the consent screen in jinear-app, which is where the user is already
 * signed in and where the sign in redirect already works.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpAuthorizationManager {

    private static final String RESPONSE_TYPE_CODE = "code";

    private final McpOauthClientService mcpOauthClientService;
    private final McpRedirectUriMatcher mcpRedirectUriMatcher;
    private final McpPkceValidator mcpPkceValidator;
    private final McpScopeService mcpScopeService;
    private final McpAuthorizationRequestService mcpAuthorizationRequestService;
    private final McpAuthorizationCodeService mcpAuthorizationCodeService;
    private final McpConnectionService mcpConnectionService;
    private final SessionInfoService sessionInfoService;
    private final McpProperties mcpProperties;
    private final FeProperties feProperties;
    private final InstanceFlagService instanceFlagService;

    /**
     * Validates an /authorize call and returns where to send the browser.
     * <p>
     * Errors split in two. Anything wrong with the client or the redirect URI cannot
     * be reported back to the client, because we have no address we trust: those throw
     * and the controller renders them. Everything after that is reported as an OAuth
     * error redirect to the validated redirect URI, which is what the RFC requires.
     */
    public String authorize(McpAuthorizeRequestVo vo) {
        assertEnabled();

        McpClientMetadataVo client = mcpOauthClientService.resolveForAuthorization(vo.getClientId());
        List<String> registeredRedirects = mcpOauthClientService.redirectUrisOf(client);
        if (!mcpRedirectUriMatcher.matchesAny(registeredRedirects, vo.getRedirectUri())) {
            log.warn("[MCP] Redirect uri is not registered. clientId: {}, redirectUri: {}", vo.getClientId(), vo.getRedirectUri());
            throw new BusinessException("mcp.error.oauth.invalid-redirect-uri");
        }

        if (!RESPONSE_TYPE_CODE.equals(vo.getResponseType())) {
            return errorRedirect(vo, "unsupported_response_type", "Only the authorization code flow is supported.");
        }
        if (Objects.isNull(vo.getCodeChallenge()) || vo.getCodeChallenge().isBlank()) {
            return errorRedirect(vo, "invalid_request", "A PKCE code challenge is required.");
        }
        if (!mcpPkceValidator.isSupportedMethod(vo.getCodeChallengeMethod())) {
            return errorRedirect(vo, "invalid_request", "Only the S256 code challenge method is supported.");
        }
        if (!isResourceAcceptable(vo.getResource())) {
            return errorRedirect(vo, "invalid_target", "The requested resource does not match this server.");
        }

        Set<String> scopes = mcpScopeService.parse(vo.getScope());
        if (scopes.isEmpty()) {
            scopes = mcpScopeService.defaultScopes();
        }

        McpAuthorizationRequest request = mcpAuthorizationRequestService.initialize(
                vo.getClientId(),
                vo.getRedirectUri(),
                scopes,
                vo.getState(),
                vo.getCodeChallenge(),
                vo.getCodeChallengeMethod(),
                vo.getResource());

        log.info("[MCP] Parked authorization request. requestId: {}, clientId: {}",
                request.getMcpAuthorizationRequestId(), vo.getClientId());
        return feProperties.getMcpConsentUrl().replace("{requestId}", request.getMcpAuthorizationRequestId());
    }

    public McpConsentInfoResponse retrieveConsentInfo(String requestId) {
        assertEnabled();
        McpAuthorizationRequest request = mcpAuthorizationRequestService.retrievePending(requestId);
        McpClientMetadataVo client = mcpOauthClientService.resolveForAuthorization(request.getClientId());
        List<String> registeredRedirects = mcpOauthClientService.redirectUrisOf(client);

        McpConsentInfoDto dto = new McpConsentInfoDto();
        dto.setRequestId(requestId);
        dto.setClientDisplayHost(displayHost(request.getClientId()));
        dto.setClientName(client.getClientName());
        dto.setClientUri(client.getClientUri());
        dto.setLogoUri(client.getLogoUri());
        dto.setPolicyUri(client.getPolicyUri());
        dto.setTosUri(client.getTosUri());
        dto.setRedirectHost(hostOfRedirect(request.getRedirectUri()));
        dto.setLoopbackOnly(mcpRedirectUriMatcher.allLoopback(registeredRedirects));
        dto.setRequestedScopes(List.copyOf(mcpScopeService.parse(request.getScope())));

        McpConsentInfoResponse response = new McpConsentInfoResponse();
        response.setMcpConsentInfoDto(dto);
        return response;
    }

    public McpConsentResponse submitConsent(McpConsentRequest consentRequest) {
        assertEnabled();
        String accountId = sessionInfoService.currentAccountId();
        McpAuthorizationRequest request = mcpAuthorizationRequestService.retrievePending(consentRequest.getRequestId());

        McpConsentResponse response = new McpConsentResponse();
        if (!Boolean.TRUE.equals(consentRequest.getApproved())) {
            mcpAuthorizationRequestService.complete(request);
            log.info("[MCP] Consent denied. requestId: {}, accountId: {}", request.getMcpAuthorizationRequestId(), accountId);
            response.setRedirectUrl(buildRedirect(request.getRedirectUri(), Map.of(
                    "error", "access_denied",
                    "error_description", "The user declined the connection."), request.getState()));
            return response;
        }

        Set<String> scopes = mcpScopeService.parse(request.getScope());
        String clientName = mcpOauthClientService.displayNameFor(request.getClientId());
        McpConnection connection = mcpConnectionService.grant(accountId, request.getClientId(), clientName, scopes);
        String code = mcpAuthorizationCodeService.issue(request, accountId, connection.getMcpConnectionId());
        mcpAuthorizationRequestService.complete(request);

        log.info("[MCP] Consent granted. requestId: {}, accountId: {}, connectionId: {}",
                request.getMcpAuthorizationRequestId(), accountId, connection.getMcpConnectionId());
        response.setRedirectUrl(buildRedirect(request.getRedirectUri(), Map.of("code", code), request.getState()));
        return response;
    }

    /**
     * Two switches, checked only here on the authorization path. The property is the hard
     * one: with it off nothing MCP answers at all. The instance flag is the administrator's
     * switch, and it gates new connections only, so turning it off stops anybody granting
     * fresh access while the connections people already made keep working until they
     * disconnect them. The transport itself does not read the flag, because that would put
     * a database read in front of every tool call.
     */
    private void assertEnabled() {
        if (!Boolean.TRUE.equals(mcpProperties.getEnabled())
                || !instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)) {
            throw new BusinessException("mcp.error.disabled");
        }
    }

    /**
     * RFC 8707 resource indicators. Clients must send this, but a few still do not, so
     * an absent value is accepted and only a mismatched one is refused.
     */
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
        String host = mcpOauthClientService.hostOf(clientId);
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

    private String errorRedirect(McpAuthorizeRequestVo vo, String error, String description) {
        log.warn("[MCP] Authorization refused. clientId: {}, error: {}", vo.getClientId(), error);
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
