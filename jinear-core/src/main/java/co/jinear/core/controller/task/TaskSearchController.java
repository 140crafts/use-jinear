package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.model.response.task.TaskSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/search")
public class TaskSearchController {

    private final TaskSearchManager taskSearchManager;

    @GetMapping("/{workspaceId}/{teamId}/{query}")
    @ResponseStatus(HttpStatus.OK)
    public TaskSearchResponse retrieveAllTasks(@PathVariable String workspaceId,
                                               @PathVariable String teamId,
                                               @PathVariable String query,
                                               @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskSearchManager.searchTask(query, workspaceId, teamId, page);
    }
}