package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceTierManager;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/internal/workspace/tier")
public class InternalWorkspaceTierController {

    private final WorkspaceTierManager workspaceTierManager;

    @PutMapping("/{workspaceId}/{workspaceTier}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTier(@PathVariable String workspaceId, @PathVariable WorkspaceTier workspaceTier) {
        return workspaceTierManager.updateTier(workspaceId, workspaceTier);
    }
}
