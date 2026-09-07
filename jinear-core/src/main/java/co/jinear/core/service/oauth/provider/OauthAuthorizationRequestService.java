package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
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

    public OauthAuthorizationRequest initialize(OauthAuthorizeRequestVo vo, Set<String> scopes) {
        OauthAuthorizationRequest request = new OauthAuthorizationRequest();
        request.setClientId(vo.getClientId());
        request.setRedirectUri(vo.getRedirectUri());
        request.setScope(oauthScopeService.format(scopes));
        request.setState(vo.getState());
        request.setCodeChallenge(vo.getCodeChallenge());
        request.setCodeChallengeMethod(vo.getCodeChallengeMethod());
        request.setResource(vo.getResource());
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
