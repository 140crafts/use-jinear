package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.model.response.task.TaskListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/list")
public class TaskListingController {

    private final TaskListingManager taskListingManager;

    @GetMapping("/{workspaceId}/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListingResponse retrieveAllTasks(@PathVariable String workspaceId,
                                                @PathVariable String teamId,
                                                @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskListingManager.retrieveAllTasks(workspaceId, teamId, page);
    }
}
