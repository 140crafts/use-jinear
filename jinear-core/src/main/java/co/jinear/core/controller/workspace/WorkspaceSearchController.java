package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceSearchManager;
import co.jinear.core.model.request.workspace.WorkspaceSearchRequest;
import co.jinear.core.model.response.workspace.WorkspaceSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/search")
public class WorkspaceSearchController {

    private final WorkspaceSearchManager workspaceSearchManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceSearchResponse search(@Valid @RequestBody WorkspaceSearchRequest workspaceSearchRequest,
                                          @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceSearchManager.search(workspaceSearchRequest, page);
    }
}
