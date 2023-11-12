package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceActivityFilterManager;
import co.jinear.core.manager.workspace.WorkspaceActivityManager;
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

    private final WorkspaceActivityManager workspaceActivityManager;
    private final WorkspaceActivityFilterManager workspaceActivityFilterManager;

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceActivityListResponse filter(@Valid @RequestBody WorkspaceActivityFilterRequest workspaceActivityFilterRequest) {
        return workspaceActivityFilterManager.filterActivities(workspaceActivityFilterRequest);
    }

    @GetMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceActivityListResponse retrieveActivities(@PathVariable String workspaceId,
                                                            @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceActivityManager.retrieveWorkspaceActivities(workspaceId, page);
    }

    @GetMapping("/{workspaceId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceActivityListResponse retrieveActivitiesFromTeam(@PathVariable String workspaceId,
                                                                    @PathVariable String teamId,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceActivityManager.retrieveWorkspaceTeamActivities(workspaceId, teamId, page);
    }

    @GetMapping("/{workspaceId}/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceActivityListResponse retrieveActivitiesFromTask(@PathVariable String workspaceId,
                                                                    @PathVariable String taskId,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceActivityManager.retrieveTaskActivities(taskId, page);
    }
}
