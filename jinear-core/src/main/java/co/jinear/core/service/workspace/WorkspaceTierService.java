package co.jinear.core.service.workspace;

import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceTierService {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceRepository workspaceRepository;

    public void updateWorkspaceTier(String workspaceId, WorkspaceTier workspaceTier) {
        log.info("Update workspace tier has started. workspaceId: {}, workspaceTier: {}", workspaceId, workspaceTier);
        Workspace workspace = workspaceRetrieveService.retrieveWorkspaceEntityWithId(workspaceId);
        workspace.setTier(workspaceTier);
        workspaceRepository.save(workspace);
        log.info("Update workspace tier has completed. workspaceId: {}, workspaceTier: {}", workspaceId, workspaceTier);
    }
}
