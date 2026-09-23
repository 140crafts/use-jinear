package co.jinear.core.service.oauth.provider;

import co.jinear.core.model.dto.oauth.OauthAuthorizationRequestDto;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
import co.jinear.core.system.oauth.OauthRedirectUriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * Turns a consent decision into the URL the client is sent back to. Granting writes the
 * connection, the authorization code and the request completion together, so it runs in one
 * transaction.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OauthConsentService {

    private static final String ERROR_ACCESS_DENIED = "access_denied";

    private final OauthClientService oauthClientService;
    private final OauthScopeService oauthScopeService;
    private final OauthConnectionService oauthConnectionService;
    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthAuthorizationRequestService oauthAuthorizationRequestService;

    @Transactional
    public String grant(OauthAuthorizationRequestDto request, String accountId) {
        Set<String> scopes = oauthScopeService.parse(request.getScope());
        String clientName = oauthClientService.displayNameFor(request.getClientId());
        OauthConnectionDto connection = oauthConnectionService.grant(accountId, request.getClientId(), clientName, scopes);
        String code = oauthAuthorizationCodeService.issue(request, accountId, connection.getOauthConnectionId());
        oauthAuthorizationRequestService.complete(request.getOauthAuthorizationRequestId());

        log.info("[OAUTH] Consent granted. requestId: {}, accountId: {}, connectionId: {}",
                request.getOauthAuthorizationRequestId(), accountId, connection.getOauthConnectionId());
        return OauthRedirectUriBuilder.buildRedirect(request.getRedirectUri(), Map.of("code", code), request.getState());
    }

    @Transactional
    public String deny(OauthAuthorizationRequestDto request, String accountId) {
        oauthAuthorizationRequestService.complete(request.getOauthAuthorizationRequestId());
        log.info("[OAUTH] Consent denied. requestId: {}, accountId: {}",
                request.getOauthAuthorizationRequestId(), accountId);
        return OauthRedirectUriBuilder.buildRedirect(request.getRedirectUri(),
                Map.of("error", ERROR_ACCESS_DENIED,
                        "error_description", "The user declined the connection."),
                request.getState());
    }
}
