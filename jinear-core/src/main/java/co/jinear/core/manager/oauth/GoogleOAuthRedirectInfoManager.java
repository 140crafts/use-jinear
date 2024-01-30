package co.jinear.core.manager.oauth;

import co.jinear.core.model.response.auth.AuthRedirectInfoResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.google.GoogleRedirectInfoService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthRedirectInfoManager {

    private final GoogleRedirectInfoService googleRedirectInfoService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;

    public AuthRedirectInfoResponse retrieveLoginRedirectUrl() {
        log.info("Retrieve login redirect url has started.");
        String redirectUrl = googleRedirectInfoService.retrieveLoginUrl();
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthRedirectInfoResponse retrieveAttachMailUrl(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        log.info("Retrieve mail attach redirect url has started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        String redirectUrl = googleRedirectInfoService.retrieveAttachMailUrl(workspaceId);
        return new AuthRedirectInfoResponse(redirectUrl);
    }

    public AuthRedirectInfoResponse retrieveAttachCalendarUrl(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        log.info("Retrieve calendar attach redirect url has started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        String redirectUrl = googleRedirectInfoService.retrieveAttachCalendarUrl(workspaceId);
        return new AuthRedirectInfoResponse(redirectUrl);
    }
}
