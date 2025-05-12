package co.jinear.core.validator.workspace;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static co.jinear.core.model.enumtype.workspace.WorkspaceContentVisibilityType.HIDDEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkspaceValidator {

    private final WorkspaceMemberService workspaceMemberService;

    public void validateHasAccess(String currentAccountId, WorkspaceDto workspaceDto) {
        validateHasAccess(currentAccountId, workspaceDto.getWorkspaceId());
    }

    public void validateHasAccess(String currentAccountId, String workspaceId) {
        workspaceMemberService.validateAccountWorkspaceMember(currentAccountId, workspaceId);
    }

    public boolean isAccountWorkspaceMember(String currentAccountId, String workspaceId) {
        return workspaceMemberService.isAccountWorkspaceMember(currentAccountId, workspaceId);
    }

    public void validateHasContentAccess(String currentAccountId, WorkspaceDto workspaceDto) {
        if (HIDDEN.equals(workspaceDto.getSettings().getContentVisibility())) {
            workspaceMemberService.validateAccountWorkspaceMember(currentAccountId, workspaceDto.getWorkspaceId());
        }
    }

    public void validateWorkspaceRoles(String accountId, String workspaceId, List<WorkspaceAccountRoleType> roleTypes) {
        workspaceMemberService.validateAccountHasRoleInWorkspace(accountId, workspaceId, roleTypes);
    }

    public boolean isWorkspaceAdminOrOwner(String accountId, String workspaceId) {
        return workspaceMemberService.doesAccountHaveWorkspaceAdminAccess(accountId, workspaceId);
    }

    public void validateWorkspaceMemberIdIsInWorkspace(String workspaceMemberId, String workspaceId){
        if (!workspaceMemberService.checkWorkspaceMemberExistsInWorkspace(workspaceMemberId,workspaceId)){
            throw new NoAccessException();
        }
    }
}
