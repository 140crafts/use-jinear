package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.request.task.TaskUpdateRequest;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task")
public class TaskController {

    private final TaskInitializeManager taskInitializeManager;
    private final TaskUpdateManager taskUpdateManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse initializeTask(@Valid @RequestBody TaskInitializeRequest taskInitializeRequest) {
        return taskInitializeManager.initializeTask(taskInitializeRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTask(@Valid @RequestBody TaskUpdateRequest taskUpdateRequest) {
        return taskUpdateManager.updateTask(taskUpdateRequest);
    }
}
