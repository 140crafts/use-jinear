package co.jinear.core.manager.oauth.provider;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.converter.oauth.OauthDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.request.oauth.OauthConsentRequest;
import co.jinear.core.model.response.oauth.OauthConsentInfoResponse;
import co.jinear.core.model.response.oauth.OauthConsentResponse;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.model.vo.oauth.OauthErrorVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.oauth.provider.OauthAuthorizationCodeService;
import co.jinear.core.service.oauth.provider.OauthAuthorizationRequestService;
import co.jinear.core.service.oauth.provider.OauthClientService;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.service.oauth.provider.OauthScopeService;
import co.jinear.core.validator.oauth.OauthAuthorizeRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAuthorizationManager {

    private final OauthAuthorizeRequestValidator oauthAuthorizeRequestValidator;
    private final OauthClientService oauthClientService;
    private final OauthScopeService oauthScopeService;
    private final OauthAuthorizationRequestService oauthAuthorizationRequestService;
    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthConnectionService oauthConnectionService;
    private final OauthDtoConverter oauthDtoConverter;
    private final SessionInfoService sessionInfoService;
    private final OauthProperties oauthProperties;
    private final FeProperties feProperties;

    public String authorize(OauthAuthorizeRequestVo vo) {
        validateOauthIsEnabled();
        oauthAuthorizeRequestValidator.validateClientAndRedirectUri(vo);

        Optional<OauthErrorVo> error = oauthAuthorizeRequestValidator.validateRequestParameters(vo);
        if (error.isPresent()) {
            return errorRedirect(vo, error.get());
        }

        Set<String> scopes = oauthScopeService.parse(vo.getScope());
        if (scopes.isEmpty()) {
            scopes = oauthScopeService.defaultScopes();
        }

        OauthAuthorizationRequest request = oauthAuthorizationRequestService.initialize(vo, scopes);
        log.info("[OAUTH] Parked authorization request. requestId: {}, clientId: {}", request.getOauthAuthorizationRequestId(), vo.getClientId());
        return feProperties.getOauthConsentUrl().replace("{requestId}", request.getOauthAuthorizationRequestId());
    }

    public OauthConsentInfoResponse retrieveConsentInfo(String requestId) {
        validateOauthIsEnabled();
        OauthAuthorizationRequest request = oauthAuthorizationRequestService.retrievePending(requestId);
        OauthClientMetadataVo client = oauthClientService.resolveForAuthorization(request.getClientId());
        List<String> registeredRedirects = oauthClientService.redirectUrisOf(client);

        OauthConsentInfoResponse response = new OauthConsentInfoResponse();
        response.setOauthConsentInfoDto(oauthDtoConverter.convert(request, client, registeredRedirects));
        return response;
    }

    public OauthConsentResponse submitConsent(OauthConsentRequest consentRequest) {
        validateOauthIsEnabled();
        String accountId = sessionInfoService.currentAccountId();
        OauthAuthorizationRequest request = oauthAuthorizationRequestService.retrievePending(consentRequest.getRequestId());

        OauthConsentResponse response = new OauthConsentResponse();
        if (!Boolean.TRUE.equals(consentRequest.getApproved())) {
            oauthAuthorizationRequestService.complete(request);
            log.info("[OAUTH] Consent denied. requestId: {}, accountId: {}", request.getOauthAuthorizationRequestId(), accountId);
            response.setRedirectUrl(buildRedirect(request.getRedirectUri(),
                    Map.of(
                            "error", "access_denied",
                            "error_description", "The user declined the connection."), request.getState()));
            return response;
        }

        Set<String> scopes = oauthScopeService.parse(request.getScope());
        String clientName = oauthClientService.displayNameFor(request.getClientId());
        OauthConnection connection = oauthConnectionService.grant(accountId, request.getClientId(), clientName, scopes);
        String code = oauthAuthorizationCodeService.issue(request, accountId, connection.getOauthConnectionId());
        oauthAuthorizationRequestService.complete(request);

        log.info("[OAUTH] Consent granted. requestId: {}, accountId: {}, connectionId: {}", request.getOauthAuthorizationRequestId(), accountId, connection.getOauthConnectionId());
        response.setRedirectUrl(buildRedirect(request.getRedirectUri(), Map.of("code", code), request.getState()));
        return response;
    }

    private void validateOauthIsEnabled() {
        if (!Boolean.TRUE.equals(oauthProperties.getEnabled())) {
            throw new BusinessException("oauth.error.disabled");
        }
    }

    private String errorRedirect(OauthAuthorizeRequestVo vo, OauthErrorVo error) {
        log.warn("[OAUTH] Authorization refused. clientId: {}, error: {}", vo.getClientId(), error.getError());
        return buildRedirect(vo.getRedirectUri(),
                Map.of("error", error.getError(), "error_description", error.getErrorDescription()),
                vo.getState());
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
