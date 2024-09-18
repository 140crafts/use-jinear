package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.request.task.*;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{taskId}/dates")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTaskDates(@PathVariable("taskId") String taskId,
                                        @Valid @RequestBody TaskDateUpdateRequest taskDateUpdateRequest) {
        return taskUpdateManager.updateTaskDates(taskId, taskDateUpdateRequest);
    }

    @PutMapping("/{taskId}/assignee")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTaskAssignee(@PathVariable("taskId") String taskId,
                                           @Valid @RequestBody TaskAssigneeUpdateRequest taskAssigneeUpdateRequest) {
        return taskUpdateManager.updateTaskAssignee(taskId, taskAssigneeUpdateRequest);
    }

    @PutMapping("/{taskId}/project-milestone")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTaskProjectAndMilestone(@PathVariable("taskId") String taskId,
                                                      @Valid @RequestBody TaskProjectAndMilestoneUpdateRequest taskProjectAndMilestoneUpdateRequest) {
        return taskUpdateManager.updateTaskProjectAndMilestone(taskId, taskProjectAndMilestoneUpdateRequest);
    }
}