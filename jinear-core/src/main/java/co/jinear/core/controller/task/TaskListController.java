package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskListManager;
import co.jinear.core.model.request.task.TaskListInitializeRequest;
import co.jinear.core.model.request.task.TaskListUpdateDueDateRequest;
import co.jinear.core.model.request.task.TaskListUpdateStateRequest;
import co.jinear.core.model.request.task.TaskListUpdateTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-list")
public class TaskListController {

    private final TaskListManager taskListManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListResponse initializeTaskList(@Valid @RequestBody TaskListInitializeRequest taskListInitializeRequest) {
        return taskListManager.initializeTaskList(taskListInitializeRequest);
    }

    @PutMapping("/update/due-date")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDueDate(@Valid @RequestBody TaskListUpdateDueDateRequest taskListInitializeRequest) {
        return taskListManager.updateDueDate(taskListInitializeRequest);
    }

    @PutMapping("/update/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitle(@Valid @RequestBody TaskListUpdateTitleRequest taskListUpdateTitleRequest) {
        return taskListManager.updateTitle(taskListUpdateTitleRequest);
    }

    @PutMapping("/update/state")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateState(@Valid @RequestBody TaskListUpdateStateRequest taskListUpdateStateRequest) {
        return taskListManager.updateState(taskListUpdateStateRequest);
    }
}
