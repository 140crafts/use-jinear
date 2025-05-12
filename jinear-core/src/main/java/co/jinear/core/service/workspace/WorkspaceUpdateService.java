package co.jinear.core.service.workspace;

import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceUpdateService {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceRepository workspaceRepository;

    public void updateWorkspaceTitle(String workspaceId, String title) {
        log.info("Update workspace title has started. workspaceId: {}, title: {}", workspaceId, title);
        Workspace workspace = workspaceRetrieveService.retrieveWorkspaceEntityWithId(workspaceId);
        workspace.setTitle(title);
        workspaceRepository.save(workspace);
    }
}
