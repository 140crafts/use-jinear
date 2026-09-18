package co.jinear.core.manager.oauth.provider;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.oauth.OauthAuthorizeRequestVoConverter;
import co.jinear.core.converter.oauth.OauthDtoConverter;
import co.jinear.core.model.dto.oauth.OauthAuthorizationRequestDto;
import co.jinear.core.model.request.oauth.OauthAuthorizeRequest;
import co.jinear.core.model.request.oauth.OauthConsentRequest;
import co.jinear.core.model.response.oauth.OauthConsentInfoResponse;
import co.jinear.core.model.response.oauth.OauthConsentResponse;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.model.vo.oauth.OauthErrorVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.oauth.provider.OauthAuthorizationRequestService;
import co.jinear.core.service.oauth.provider.OauthClientService;
import co.jinear.core.service.oauth.provider.OauthConsentService;
import co.jinear.core.service.oauth.provider.OauthScopeService;
import co.jinear.core.system.oauth.OauthRedirectUriBuilder;
import co.jinear.core.validator.oauth.OauthAuthorizeRequestValidator;
import co.jinear.core.validator.oauth.OauthEnabledValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAuthorizationManager {

    private final OauthAuthorizeRequestValidator oauthAuthorizeRequestValidator;
    private final OauthEnabledValidator oauthEnabledValidator;
    private final OauthClientService oauthClientService;
    private final OauthScopeService oauthScopeService;
    private final OauthAuthorizationRequestService oauthAuthorizationRequestService;
    private final OauthConsentService oauthConsentService;
    private final OauthDtoConverter oauthDtoConverter;
    private final OauthAuthorizeRequestVoConverter oauthAuthorizeRequestVoConverter;
    private final SessionInfoService sessionInfoService;
    private final FeProperties feProperties;

    public String authorize(OauthAuthorizeRequest oauthAuthorizeRequest) {
        oauthEnabledValidator.validateOauthIsEnabled();
        OauthAuthorizeRequestVo vo = oauthAuthorizeRequestVoConverter.map(oauthAuthorizeRequest);
        oauthAuthorizeRequestValidator.validateClientAndRedirectUri(vo);

        Optional<OauthErrorVo> error = oauthAuthorizeRequestValidator.validateRequestParameters(vo);
        if (error.isPresent()) {
            return errorRedirect(vo, error.get());
        }

        Set<String> scopes = oauthScopeService.parse(vo.getScope());
        if (scopes.isEmpty()) {
            scopes = oauthScopeService.defaultScopes();
        }

        OauthAuthorizationRequestDto request = oauthAuthorizationRequestService.initialize(vo, scopes);
        log.info("[OAUTH] Parked authorization request. requestId: {}, clientId: {}",
                request.getOauthAuthorizationRequestId(), vo.getClientId());
        return feProperties.getOauthConsentUrl()
                .replace("{requestId}", request.getOauthAuthorizationRequestId());
    }

    public OauthConsentInfoResponse retrieveConsentInfo(String requestId) {
        oauthEnabledValidator.validateOauthIsEnabled();
        OauthAuthorizationRequestDto request = oauthAuthorizationRequestService.retrievePending(requestId);
        OauthClientMetadataVo client = oauthClientService.resolveForAuthorization(request.getClientId());
        List<String> registeredRedirects = oauthClientService.redirectUrisOf(client);

        OauthConsentInfoResponse response = new OauthConsentInfoResponse();
        response.setOauthConsentInfoDto(oauthDtoConverter.convert(request, client, registeredRedirects));
        return response;
    }

    public OauthConsentResponse submitConsent(OauthConsentRequest oauthConsentRequest) {
        oauthEnabledValidator.validateOauthIsEnabled();
        String accountId = sessionInfoService.currentAccountId();
        OauthAuthorizationRequestDto request =
                oauthAuthorizationRequestService.retrievePending(oauthConsentRequest.getRequestId());

        OauthConsentResponse response = new OauthConsentResponse();
        response.setRedirectUrl(Boolean.TRUE.equals(oauthConsentRequest.getApproved())
                ? oauthConsentService.grant(request, accountId)
                : oauthConsentService.deny(request, accountId));
        return response;
    }

    private String errorRedirect(OauthAuthorizeRequestVo vo, OauthErrorVo error) {
        log.warn("[OAUTH] Authorization refused. clientId: {}, error: {}", vo.getClientId(), error.getError());
        return OauthRedirectUriBuilder.buildRedirect(vo.getRedirectUri(),
                Map.of("error", error.getError(), "error_description", error.getErrorDescription()),
                vo.getState());
    }
}
