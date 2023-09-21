package co.jinear.core.validator.workspace;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.service.workspace.member.WorkspaceInvitationListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceInvitationValidator {

    private final WorkspaceInvitationListingService workspaceInvitationListingService;

    public void validateActiveInviteExists(String workspaceId, String email) {
        log.info("Validate active invite exists has started. workspaceId: {}, email: {}", workspaceId, email);
        if (workspaceInvitationListingService.checkActiveInviteExists(workspaceId,email)){
            throw new BusinessException("workspace.invitation.active-invitation-exists");
        }
    }
}
