package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskActivityRetrieveManager;
import co.jinear.core.model.response.task.TaskActivityRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/activity")
public class TaskActivityController {

    private final TaskActivityRetrieveManager taskActivityRetrieveManager;

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskActivityRetrieveResponse retrieveTaskActivity(@PathVariable String taskId) {
        return taskActivityRetrieveManager.retrieveTaskActivity(taskId);
    }
}