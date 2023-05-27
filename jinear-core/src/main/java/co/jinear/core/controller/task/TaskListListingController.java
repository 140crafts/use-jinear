package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskListListingManager;
import co.jinear.core.model.response.task.TaskListListingPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-list/list")
public class TaskListListingController {

    private final TaskListListingManager taskListListingManager;

    @GetMapping("/{workspaceId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListListingPaginatedResponse retrieveAll(@PathVariable String workspaceId,
                                                        @PathVariable String teamId,
                                                        @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskListListingManager.retrieveAll(workspaceId, teamId, page);
    }
}
