package co.jinear.core.service.mcp.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpAuthorizationRequest;
import co.jinear.core.repository.mcp.McpAuthorizationRequestRepository;
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
public class McpAuthorizationRequestService {

    private final McpAuthorizationRequestRepository mcpAuthorizationRequestRepository;
    private final McpProperties mcpProperties;
    private final McpScopeService mcpScopeService;

    public McpAuthorizationRequest initialize(String clientId,
                                              String redirectUri,
                                              Set<String> scopes,
                                              String state,
                                              String codeChallenge,
                                              String codeChallengeMethod,
                                              String resource) {
        McpAuthorizationRequest request = new McpAuthorizationRequest();
        request.setClientId(clientId);
        request.setRedirectUri(redirectUri);
        request.setScope(mcpScopeService.format(scopes));
        request.setState(state);
        request.setCodeChallenge(codeChallenge);
        request.setCodeChallengeMethod(codeChallengeMethod);
        request.setResource(resource);
        request.setExpiresAt(DateHelper.addMinutes(DateHelper.now(), mcpProperties.getAuthorizationRequestValidityMinutes()));
        return mcpAuthorizationRequestRepository.save(request);
    }

    public McpAuthorizationRequest retrievePending(String requestId) {
        McpAuthorizationRequest request = mcpAuthorizationRequestRepository
                .findByMcpAuthorizationRequestIdAndPassiveIdIsNull(requestId)
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.unknown-authorization-request"));
        if (Objects.nonNull(request.getCompletedAt())) {
            throw new BusinessException("mcp.error.oauth.authorization-request-already-used");
        }
        if (request.getExpiresAt().before(DateHelper.now())) {
            throw new BusinessException("mcp.error.oauth.authorization-request-expired");
        }
        return request;
    }

    public void complete(McpAuthorizationRequest request) {
        request.setCompletedAt(DateHelper.now());
        mcpAuthorizationRequestRepository.save(request);
    }

    public int purgeExpiredBefore(Date before) {
        return mcpAuthorizationRequestRepository.deleteAllExpiredBefore(before);
    }
}
