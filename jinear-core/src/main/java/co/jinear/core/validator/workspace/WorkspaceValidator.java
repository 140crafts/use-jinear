package co.jinear.core.validator.workspace;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static co.jinear.core.model.enumtype.workspace.WorkspaceContentVisibilityType.HIDDEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkspaceValidator {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;

    public void validateHasAccess(String currentAccountId, WorkspaceDto workspaceDto) {
        validateHasAccess(currentAccountId, workspaceDto.getWorkspaceId());
    }

    public void validateHasAccess(String currentAccountId, String workspaceId) {
        workspaceMemberService.validateAccountWorkspaceMember(currentAccountId, workspaceId);
    }

    public void validateHasContentAccess(String currentAccountId, WorkspaceDto workspaceDto) {
        if (HIDDEN.equals(workspaceDto.getSettings().getContentVisibility())) {
            workspaceMemberService.validateAccountWorkspaceMember(currentAccountId, workspaceDto.getWorkspaceId());
        }
    }

    public void validateWorkspaceIsNotPersonal(String workspaceId){
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        if (Boolean.TRUE.equals(workspaceDto.getIsPersonal())){
            throw new BusinessException();
        }
    }
}
