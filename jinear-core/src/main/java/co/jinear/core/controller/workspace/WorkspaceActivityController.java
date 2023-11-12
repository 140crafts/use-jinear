package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceActivityFilterManager;
import co.jinear.core.model.request.workspace.WorkspaceActivityFilterRequest;
import co.jinear.core.model.response.workspace.WorkspaceActivityListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/activity")
public class WorkspaceActivityController {

    private final WorkspaceActivityFilterManager workspaceActivityFilterManager;

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceActivityListResponse filter(@Valid @RequestBody WorkspaceActivityFilterRequest workspaceActivityFilterRequest) {
        return workspaceActivityFilterManager.filterActivities(workspaceActivityFilterRequest);
    }
}
