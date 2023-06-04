package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.model.request.task.TaskBoardInitializeRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateDueDateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateStateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskBoardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-board")
public class TaskBoardController {

    private final TaskBoardManager taskBoardManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskBoardResponse initializeTaskBoard(@Valid @RequestBody TaskBoardInitializeRequest taskBoardInitializeRequest) {
        return taskBoardManager.initializeTaskBoard(taskBoardInitializeRequest);
    }

    @PutMapping("/update/due-date")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDueDate(@Valid @RequestBody TaskBoardUpdateDueDateRequest taskBoardUpdateDueDateRequest) {
        return taskBoardManager.updateDueDate(taskBoardUpdateDueDateRequest);
    }

    @PutMapping("/update/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitle(@Valid @RequestBody TaskBoardUpdateTitleRequest taskListUpdateTitleRequest) {
        return taskBoardManager.updateTitle(taskListUpdateTitleRequest);
    }

    @PutMapping("/update/state")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateState(@Valid @RequestBody TaskBoardUpdateStateRequest taskListUpdateStateRequest) {
        return taskBoardManager.updateState(taskListUpdateStateRequest);
    }
}
