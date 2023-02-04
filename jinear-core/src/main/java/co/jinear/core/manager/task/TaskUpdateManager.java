package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskDatesUpdateVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.request.task.TaskAssigneeUpdateRequest;
import co.jinear.core.model.request.task.TaskDateUpdateRequest;
import co.jinear.core.model.request.task.TaskUpdateDescriptionRequest;
import co.jinear.core.model.request.task.TaskUpdateTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskAssigneeUpdateVo;
import co.jinear.core.model.vo.task.TaskDatesUpdateVo;
import co.jinear.core.model.vo.task.TaskDescriptionUpdateVo;
import co.jinear.core.model.vo.task.TaskTitleUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskUpdateService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

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
    private final TopicRetrieveService topicRetrieveService;
    private final TaskDatesUpdateVoConverter taskDatesUpdateVoConverter;

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
        log.info("Update task workflow status has started. accountId: {}, taskId: {}, newWorkflowStatusId: {}", currentAccountId, taskId, workflowStatusId);
        TaskDto taskDto = taskUpdateService.updateTaskWorkflow(taskId, workflowStatusId);
        taskActivityService.initializeStatusUpdateActivity(currentAccountId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskTopic(String taskId, String topicId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateTaskAndTopicIsInSameTeam(taskDtoBeforeUpdate, topicId);
        log.info("Update task topic has started. accountId: {}, taskId: {}, topicId: {}", currentAccountId, taskId, topicId);
        TaskDto taskDto = taskUpdateService.updateTaskTopic(taskId, topicId);
        taskActivityService.initializeTopicUpdateActivity(currentAccountId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse removeTaskTopic(String taskId) {
        return updateTaskTopic(taskId, null);
    }

    public TaskResponse updateTaskAssignedDate(String taskId, TaskDateUpdateRequest taskDateUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateDueDateIsAfterAssignedDate(taskDateUpdateRequest.getDate(), taskDtoBeforeUpdate.getDueDate());
        log.info("Update task assigned date has started. accountId: {}, taskId: {}", currentAccountId, taskId);
        TaskDatesUpdateVo taskDatesUpdateVo = taskDatesUpdateVoConverter.map(taskDateUpdateRequest, taskId);
        TaskDto taskDto = taskUpdateService.updateTaskAssignedDate(taskDatesUpdateVo);
        taskActivityService.initializeAssignedDateUpdateActivity(currentAccountId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskDueDate(String taskId, TaskDateUpdateRequest taskDateUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateDueDateIsAfterAssignedDate(taskDtoBeforeUpdate.getAssignedDate(), taskDateUpdateRequest.getDate());
        log.info("Update task due date has started. accountId: {}, taskId: {}", currentAccountId, taskId);
        TaskDatesUpdateVo taskDatesUpdateVo = taskDatesUpdateVoConverter.map(taskDateUpdateRequest, taskId);
        TaskDto taskDto = taskUpdateService.updateTaskDueDate(taskDatesUpdateVo);
        taskActivityService.initializeDueDateUpdateActivity(currentAccountId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskAssignee(String taskId, TaskAssigneeUpdateRequest taskAssigneeUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateNewAssigneeHasTaskAccess(taskId, taskAssigneeUpdateRequest);
        log.info("Update task assignee has started. accountId: {}, taskId: {}, taskAssigneeUpdateRequest: {}", currentAccountId, taskId, taskAssigneeUpdateRequest);
        TaskAssigneeUpdateVo taskAssigneeUpdateVo = taskDatesUpdateVoConverter.map(taskAssigneeUpdateRequest, taskId);
        TaskDto taskDto = taskUpdateService.updateTaskAssignee(taskAssigneeUpdateVo);
        taskActivityService.initializeAssigneeUpdateActivity(currentAccountId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    private TaskDto validateAccess(String taskId, String currentAccountId) {
        TaskDto taskDto = taskRetrieveService.retrieve(taskId);
        workspaceValidator.validateHasAccess(currentAccountId, taskDto.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(currentAccountId, taskDto.getTeamId());
        return taskDto;
    }

    private void validateTaskAndTopicIsInSameTeam(TaskDto taskDto, String topicId) {
        String taskTeamId = taskDto.getTeamId();
        if (Objects.nonNull(topicId)) {
            topicRetrieveService.retrieveOptional(topicId)
                    .map(TopicDto::getTeamId)
                    .filter(taskTeamId::equalsIgnoreCase)
                    .orElseThrow(NoAccessException::new);
        }
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

    private void validateNewAssigneeHasTaskAccess(String taskId, TaskAssigneeUpdateRequest taskAssigneeUpdateRequest) {
        Optional.of(taskAssigneeUpdateRequest)
                .map(TaskAssigneeUpdateRequest::getAssigneeId)
                .ifPresent(newAssignee -> validateAccess(taskId, newAssignee));
    }

    private void validateDueDateIsAfterAssignedDate(ZonedDateTime assignedDate, ZonedDateTime dueDate) {
        if (Objects.nonNull(assignedDate) && Objects.nonNull(dueDate) && assignedDate.isAfter(dueDate)) {
            throw new BusinessException();
        }
    }
}
