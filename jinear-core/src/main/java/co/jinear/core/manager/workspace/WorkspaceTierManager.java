package co.jinear.core.manager.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.workspace.WorkspaceTierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceTierManager {

    private final WorkspaceTierService workspaceTierService;

    public BaseResponse updateTier(String workspaceId, WorkspaceTier workspaceTier) {
        log.info("Update workspace tier has started. workspaceId: {}, workspaceTier: {}", workspaceId, workspaceTier);
        workspaceTierService.updateWorkspaceTier(workspaceId, workspaceTier);
        return new BaseResponse();
    }
}
