package co.jinear.core.service.workspace;

import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.repository.WorkspaceRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.team.TeamDeleteService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceDeleteService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final TeamDeleteService teamDeleteService;
    private final PassiveService passiveService;

    @Transactional
    public String deleteWorkspace(String workspaceId, String responsibleAccountId) {
        log.info("Delete workspace has started. workspaceId: {}, responsibleAccountId: {}", workspaceId, responsibleAccountId);
        Workspace workspace = workspaceRetrieveService.retrieveWorkspaceEntityWithId(workspaceId);
        String passiveId = passiveService.createUserActionPassive(responsibleAccountId);
        workspace.setPassiveId(passiveId);
        workspaceRepository.save(workspace);
        workspaceMemberService.removeAllMembershipsOfAWorkspace(workspaceId, passiveId);
        teamDeleteService.deleteAllWorkspaceTeams(workspaceId, passiveId);
        log.info("Delete workspace has completed. workspaceId: {}, passiveId: {}", workspaceId, passiveId);
        return passiveId;
    }
}
