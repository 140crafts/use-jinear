package co.jinear.core.manager.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskUpdateDescriptionRequest;
import co.jinear.core.model.request.task.TaskUpdateTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskDescriptionUpdateVo;
import co.jinear.core.model.vo.task.TaskTitleUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskUpdateService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskUpdateService taskUpdateService;
    private final TaskActivityService taskActivityService;

    public BaseResponse updateTaskTitle(String taskId, TaskUpdateTitleRequest taskUpdateTitleRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        log.info("Update task description has started. accountId: {}", currentAccountId);
        TaskTitleUpdateVo taskTitleUpdateVo = mapRequest(taskId, taskUpdateTitleRequest);
        TaskDto taskDtoAfterUpdate = taskUpdateService.updateTaskTitle(taskTitleUpdateVo);
        taskActivityService.initializeEditTitleActivity(currentAccountId, taskDtoBeforeUpdate, taskDtoAfterUpdate);
        return new BaseResponse();
    }

    public BaseResponse updateTaskDescription(String taskId, TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        log.info("Update task description has started. accountId: {}", currentAccountId);
        TaskDescriptionUpdateVo taskDescriptionUpdateVo = mapRequest(taskId, taskUpdateDescriptionRequest);
        TaskDto taskDtoAfterUpdate = taskUpdateService.updateTaskDescription(taskDescriptionUpdateVo);
        taskActivityService.initializeEditDescriptionActivity(currentAccountId, taskDtoBeforeUpdate, taskDtoAfterUpdate);
        return new BaseResponse();
    }

    public TaskResponse updateTaskWorkflowStatus(String taskId, String workflowStatusId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        log.info("Update task workflow status has started. accountId: {}, taskId: {}, newWorkflowStatusId:", currentAccountId, taskId, workflowStatusId);
        TaskDto taskDto = taskUpdateService.updateTaskWorkflow(taskId, workflowStatusId);
        taskActivityService.initializeStatusUpdateActivity(currentAccountId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    private TaskDto validateAccess(String taskId, String currentAccountId) {
        TaskDto taskDto = taskRetrieveService.retrieve(taskId);
        workspaceValidator.validateHasAccess(currentAccountId, taskDto.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(currentAccountId, taskDto.getTeamId());
        return taskDto;
    }

    private TaskDescriptionUpdateVo mapRequest(String taskId, TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
        TaskDescriptionUpdateVo taskDescriptionUpdateVo = new TaskDescriptionUpdateVo();
        taskDescriptionUpdateVo.setTaskId(taskId);
        taskDescriptionUpdateVo.setDescription(taskUpdateDescriptionRequest.getDescription());
        return taskDescriptionUpdateVo;
    }

    private TaskTitleUpdateVo mapRequest(String taskId, TaskUpdateTitleRequest taskUpdateTitleRequest) {
        TaskTitleUpdateVo taskTitleUpdateVo = new TaskTitleUpdateVo();
        taskTitleUpdateVo.setTaskId(taskId);
        taskTitleUpdateVo.setTitle(taskUpdateTitleRequest.getTitle());
        return taskTitleUpdateVo;
    }

    private TaskResponse mapResponse(TaskDto taskDto) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskDto(taskDto);
        return taskResponse;
    }
}
