package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.model.request.task.TaskSearchRequest;
import co.jinear.core.model.response.task.TaskSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/search")
public class TaskSearchController {

    private final TaskSearchManager taskSearchManager;

    @PostMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskSearchResponse searchTasks(@Valid @RequestBody TaskSearchRequest taskSearchRequest,
                                          @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskSearchManager.searchTask(taskSearchRequest, page);
    }
}