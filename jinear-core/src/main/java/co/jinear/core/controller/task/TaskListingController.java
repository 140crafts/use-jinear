package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/list")
public class TaskListingController {

    private final TaskListingManager taskListingManager;

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public TaskListingPaginatedResponse filter(@Valid @RequestBody TaskFilterRequest taskFilterRequest) {
        return taskListingManager.filterTasks(taskFilterRequest);
    }
}
