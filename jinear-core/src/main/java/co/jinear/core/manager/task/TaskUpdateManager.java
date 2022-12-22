package co.jinear.core.manager.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskUpdateDescriptionRequest;
import co.jinear.core.model.request.task.TaskUpdateRequest;
import co.jinear.core.model.request.task.TaskUpdateTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskDescriptionUpdateVo;
import co.jinear.core.model.vo.task.TaskTitleUpdateVo;
import co.jinear.core.model.vo.task.TaskUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskUpdateService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateManager {

    private final ModelMapper modelMapper;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskUpdateService taskUpdateService;

    public TaskResponse updateTask(TaskUpdateRequest taskUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskUpdateRequest, currentAccountId);
        log.info("Update task has started. accountId: {}, taskUpdateRequest: {}", currentAccountId, taskUpdateRequest);
        TaskUpdateVo taskUpdateVo = modelMapper.map(taskUpdateRequest, TaskUpdateVo.class);
        TaskDto taskDto = taskUpdateService.updateTask(taskUpdateVo);
        return mapResponse(taskDto);
    }

    public BaseResponse updateTaskDescription(String taskId, TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskId, currentAccountId);
        log.info("Update task description has started. accountId: {}", currentAccountId);
        TaskDescriptionUpdateVo taskDescriptionUpdateVo = new TaskDescriptionUpdateVo();
        taskDescriptionUpdateVo.setTaskId(taskId);
        taskDescriptionUpdateVo.setDescription(taskUpdateDescriptionRequest.getDescription());
        taskUpdateService.updateTaskDescription(taskDescriptionUpdateVo);
        return new BaseResponse();
    }

    public BaseResponse updateTaskTitle(String taskId, TaskUpdateTitleRequest taskUpdateTitleRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskId, currentAccountId);
        log.info("Update task description has started. accountId: {}", currentAccountId);
        TaskTitleUpdateVo taskTitleUpdateVo = new TaskTitleUpdateVo();
        taskTitleUpdateVo.setTaskId(taskId);
        taskTitleUpdateVo.setTitle(taskUpdateTitleRequest.getTitle());
        taskUpdateService.updateTaskTitle(taskTitleUpdateVo);
        return new BaseResponse();
    }

    public TaskResponse updateTaskWorkflowStatus(String taskId, String workflowStatusId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskId, currentAccountId);
        log.info("Update task workflow status has started. accountId: {}, taskId: {}, newWorkflowStatusId:", currentAccountId, taskId, workflowStatusId);
        TaskDto taskDto = taskUpdateService.updateTaskWorkflow(taskId, workflowStatusId);
        return mapResponse(taskDto);
    }

    private void validateAccess(TaskUpdateRequest taskUpdateRequest, String currentAccountId) {
        validateAccess(taskUpdateRequest.getTaskId(), currentAccountId);
    }

    private void validateAccess(String taskId, String currentAccountId) {
        TaskDto taskDto = taskRetrieveService.retrieve(taskId);
        workspaceValidator.validateHasAccess(currentAccountId, taskDto.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(currentAccountId, taskDto.getTeamId());
    }

    private TaskResponse mapResponse(TaskDto taskDto) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskDto(taskDto);
        return taskResponse;
    }
}
