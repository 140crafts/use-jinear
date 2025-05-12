package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/workflow-status")
public class TaskWorkflowStatusController {

    private final TaskUpdateManager taskUpdateManager;

    @PutMapping("/{taskId}/{workflowStatusId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTaskWorkflowStatus(@PathVariable String taskId,
                                                 @PathVariable String workflowStatusId) {
        return taskUpdateManager.updateTaskWorkflowStatus(taskId, workflowStatusId);
    }
}
