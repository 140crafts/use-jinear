package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.repository.oauth.OauthAuthorizationRequestRepository;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAuthorizationRequestService {

    private final OauthAuthorizationRequestRepository oauthAuthorizationRequestRepository;
    private final OauthProperties oauthProperties;
    private final OauthScopeService oauthScopeService;

    public OauthAuthorizationRequest initialize(String clientId,
                                              String redirectUri,
                                              Set<String> scopes,
                                              String state,
                                              String codeChallenge,
                                              String codeChallengeMethod,
                                              String resource) {
        OauthAuthorizationRequest request = new OauthAuthorizationRequest();
        request.setClientId(clientId);
        request.setRedirectUri(redirectUri);
        request.setScope(oauthScopeService.format(scopes));
        request.setState(state);
        request.setCodeChallenge(codeChallenge);
        request.setCodeChallengeMethod(codeChallengeMethod);
        request.setResource(resource);
        request.setExpiresAt(DateHelper.addMinutes(DateHelper.now(), oauthProperties.getAuthorizationRequestValidityMinutes()));
        return oauthAuthorizationRequestRepository.save(request);
    }

    public OauthAuthorizationRequest retrievePending(String requestId) {
        OauthAuthorizationRequest request = oauthAuthorizationRequestRepository
                .findByOauthAuthorizationRequestIdAndPassiveIdIsNull(requestId)
                .orElseThrow(() -> new BusinessException("oauth.error.unknown-authorization-request"));
        if (Objects.nonNull(request.getCompletedAt())) {
            throw new BusinessException("oauth.error.authorization-request-already-used");
        }
        if (request.getExpiresAt().before(DateHelper.now())) {
            throw new BusinessException("oauth.error.authorization-request-expired");
        }
        return request;
    }

    public void complete(OauthAuthorizationRequest request) {
        request.setCompletedAt(DateHelper.now());
        oauthAuthorizationRequestRepository.save(request);
    }

    public int purgeExpiredBefore(Date before) {
        return oauthAuthorizationRequestRepository.deleteAllExpiredBefore(before);
    }
}
