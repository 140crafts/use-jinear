package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.request.task.TaskUpdateDescriptionRequest;
import co.jinear.core.model.request.task.TaskUpdateTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/update")
public class TaskUpdateController {

    private final TaskUpdateManager taskUpdateManager;

    @PutMapping("/{taskId}/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTaskTitle(@PathVariable("taskId") String taskId,
                                        @Valid @RequestBody TaskUpdateTitleRequest taskUpdateTitleRequest) {
        return taskUpdateManager.updateTaskTitle(taskId, taskUpdateTitleRequest);
    }

    @PutMapping("/{taskId}/description")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTaskDescription(@PathVariable("taskId") String taskId,
                                              @Valid @RequestBody TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
        return taskUpdateManager.updateTaskDescription(taskId, taskUpdateDescriptionRequest);
    }
}
