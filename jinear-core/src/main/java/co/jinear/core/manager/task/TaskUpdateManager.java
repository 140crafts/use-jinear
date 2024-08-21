package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskDatesUpdateVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.UpdateTaskWorkflowDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.request.task.*;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskAssigneeUpdateVo;
import co.jinear.core.model.vo.task.TaskDatesUpdateVo;
import co.jinear.core.model.vo.task.TaskDescriptionUpdateVo;
import co.jinear.core.model.vo.task.TaskTitleUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.project.MilestoneRetrieveService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskUpdateService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.system.util.ZonedDateHelper;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
    private final AccountRetrieveService accountRetrieveService;
    private final PassiveService passiveService;
    private final ProjectRetrieveService projectRetrieveService;
    private final MilestoneRetrieveService milestoneRetrieveService;

    public BaseResponse updateTaskTitle(String taskId, TaskUpdateTitleRequest taskUpdateTitleRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        log.info("Update task description has started. accountId: {}", currentAccountId);
        TaskTitleUpdateVo taskTitleUpdateVo = mapRequest(taskId, taskUpdateTitleRequest);
        TaskDto taskDtoAfterUpdate = taskUpdateService.updateTaskTitle(taskTitleUpdateVo);
        taskActivityService.initializeEditTitleActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDtoAfterUpdate);
        return new BaseResponse();
    }

    public BaseResponse updateTaskDescription(String taskId, TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        log.info("Update task description has started. accountId: {}", currentAccountId);
        TaskDescriptionUpdateVo taskDescriptionUpdateVo = mapRequest(taskId, taskUpdateDescriptionRequest);
        TaskDto taskDtoAfterUpdate = taskUpdateService.updateTaskDescription(taskDescriptionUpdateVo);
        taskActivityService.initializeEditDescriptionActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDtoAfterUpdate);
        return new BaseResponse();
    }

    public TaskResponse updateTaskWorkflowStatus(String taskId, String workflowStatusId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        log.info("Update task workflow status has started. accountId: {}, taskId: {}, newWorkflowStatusId: {}", currentAccountId, taskId, workflowStatusId);
        UpdateTaskWorkflowDto updateTaskWorkflowDto = taskUpdateService.updateTaskWorkflow(taskId, workflowStatusId);
        TaskDto taskDto = updateTaskWorkflowDto.getTaskDto();
        taskActivityService.initializeStatusUpdateActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDto);
        assignRemindersPassiveIdOwnershipIfExists(currentAccountId, updateTaskWorkflowDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskTopic(String taskId, String topicId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateTaskAndTopicIsInSameTeam(taskDtoBeforeUpdate, topicId);
        log.info("Update task topic has started. accountId: {}, taskId: {}, topicId: {}", currentAccountId, taskId, topicId);
        TaskDto taskDto = taskUpdateService.updateTaskTopic(taskId, topicId);
        taskActivityService.initializeTopicUpdateActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse removeTaskTopic(String taskId) {
        return updateTaskTopic(taskId, null);
    }

    public TaskResponse updateTaskDates(String taskId, TaskDateUpdateRequest taskDateUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        PlainAccountProfileDto accountDto = accountRetrieveService.retrievePlainAccountProfile(currentAccountId);
        validateDueDateIsAfterAssignedDate(taskDateUpdateRequest, accountDto.getTimeZone());
        log.info("Update task dates has started. accountId: {}, taskId: {}", currentAccountId, taskId);
        TaskDatesUpdateVo taskDatesUpdateVo = taskDatesUpdateVoConverter.map(taskDateUpdateRequest, taskId);
        TaskDto taskDto = taskUpdateService.updateTaskDates(taskDatesUpdateVo);
        taskActivityService.initializeDatesUpdateActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskAssignee(String taskId, TaskAssigneeUpdateRequest taskAssigneeUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateNewAssigneeHasTaskAccess(taskId, taskAssigneeUpdateRequest);
        log.info("Update task assignee has started. accountId: {}, taskId: {}, taskAssigneeUpdateRequest: {}", currentAccountId, taskId, taskAssigneeUpdateRequest);
        TaskAssigneeUpdateVo taskAssigneeUpdateVo = taskDatesUpdateVoConverter.map(taskAssigneeUpdateRequest, taskId);
        TaskDto taskDto = taskUpdateService.updateTaskAssignee(taskAssigneeUpdateVo);
        taskActivityService.initializeAssigneeUpdateActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskProject(String taskId, TaskProjectUpdateRequest taskProjectUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateProjectAndTaskWorkspaceAndTeams(taskProjectUpdateRequest, taskDtoBeforeUpdate);
        log.info("Update task project has started. accountId: {}, taskId: {}, taskProjectUpdateRequest: {}", currentAccountId, taskId, taskProjectUpdateRequest);
        TaskDto taskDto = taskUpdateService.updateTaskProject(taskId, taskProjectUpdateRequest.getProjectId());
        taskActivityService.initializeTaskProjectUpdateActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDto);
        return mapResponse(taskDto);
    }

    public TaskResponse updateTaskMilestone(String taskId, TaskMilestoneUpdateRequest taskMilestoneUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDtoBeforeUpdate = validateAccess(taskId, currentAccountId);
        validateMilestoneProjectAndTaskWorkspaceAndTeams(taskMilestoneUpdateRequest, taskDtoBeforeUpdate);
        log.info("Update task milestone has started. accountId: {}, taskId: {}, taskMilestoneUpdateRequest: {}", currentAccountId, taskId, taskMilestoneUpdateRequest);
        TaskDto taskDto = taskUpdateService.updateTaskMilestone(taskId, taskMilestoneUpdateRequest.getMilestoneId());
        taskActivityService.initializeTaskMilestoneUpdateActivity(currentAccountId, currentAccountSessionId, taskDtoBeforeUpdate, taskDto);
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

    private void validateDueDateIsAfterAssignedDate(TaskDateUpdateRequest taskDateUpdateRequest, String timeZone) {
        ZonedDateTime assignedDate = Optional.of(taskDateUpdateRequest)
                .map(TaskDateUpdateRequest::getAssignedDate)
                .map(date -> ZonedDateHelper.atTimeZone(date, timeZone))
                .orElse(null);
        ZonedDateTime dueDate = Optional.of(taskDateUpdateRequest)
                .map(TaskDateUpdateRequest::getDueDate)
                .map(date -> ZonedDateHelper.atTimeZone(date, timeZone))
                .orElse(null);
        if (Objects.isNull(assignedDate) || Objects.isNull(dueDate)) {
            return;
        }

        boolean isSameDay = checkIfDueDateAndAssignedDateIsSameDay(assignedDate, dueDate);
        boolean isDueDatePrecise = Boolean.TRUE.equals(taskDateUpdateRequest.getHasPreciseDueDate());
        boolean isDueDateNotPreciseAndBothDatesAreSameDay = isSameDay && !isDueDatePrecise;
        boolean isAssignedDateAfter = assignedDate.isAfter(dueDate);
        if (isAssignedDateAfter && !isDueDateNotPreciseAndBothDatesAreSameDay) {
            throw new BusinessException("task.reminder.due-date-not-future");
        }
    }

    private boolean checkIfDueDateAndAssignedDateIsSameDay(ZonedDateTime assignedDate, ZonedDateTime dueDate) {
        if (Objects.nonNull(assignedDate) && Objects.nonNull(dueDate)) {
            return dueDate.truncatedTo(ChronoUnit.DAYS).equals(assignedDate.truncatedTo(ChronoUnit.DAYS));
        }
        return false;
    }

    private void assignRemindersPassiveIdOwnershipIfExists(String currentAccountId, UpdateTaskWorkflowDto updateTaskWorkflowDto) {
        if (Objects.nonNull(updateTaskWorkflowDto.getRemindersPassiveId())) {
            passiveService.assignOwnership(updateTaskWorkflowDto.getRemindersPassiveId(), currentAccountId);
        }
    }

    private void validateProjectAndTaskWorkspaceAndTeams(TaskProjectUpdateRequest taskProjectUpdateRequest, TaskDto taskDtoBeforeUpdate) {
        Optional.of(taskProjectUpdateRequest)
                .map(TaskProjectUpdateRequest::getProjectId)
                .filter(StringUtils::isNotBlank)
                .map(projectRetrieveService::retrieve)
                .ifPresent(projectDto -> {
                    validateTaskAndProjectWorkspaceIdsAreSame(taskDtoBeforeUpdate.getWorkspaceId(), projectDto);
                    validateTaskTeamWithinProjectTeams(taskDtoBeforeUpdate.getTeamId(), projectDto);
                });
    }

    private void validateMilestoneProjectAndTaskWorkspaceAndTeams(TaskMilestoneUpdateRequest taskMilestoneUpdateRequest, TaskDto taskDtoBeforeUpdate) {
        Optional.of(taskMilestoneUpdateRequest)
                .map(TaskMilestoneUpdateRequest::getMilestoneId)
                .filter(StringUtils::isNotBlank)
                .map(milestoneRetrieveService::retrieve)
                .map(MilestoneDto::getProjectId)
                .map(milestoneProjectId -> {
                    if (!milestoneProjectId.equalsIgnoreCase(taskDtoBeforeUpdate.getProjectId())) {
                        throw new BusinessException("project.assign-task.task-project-is-not-same-with-milestone-project");
                    }
                    return milestoneProjectId;
                })
                .map(projectRetrieveService::retrieve)
                .ifPresent(projectDto -> {
                    validateTaskAndProjectWorkspaceIdsAreSame(taskDtoBeforeUpdate.getWorkspaceId(), projectDto);
                    validateTaskTeamWithinProjectTeams(taskDtoBeforeUpdate.getTeamId(), projectDto);
                });
    }

    private void validateTaskAndProjectWorkspaceIdsAreSame(String taskWorkspaceId, ProjectDto projectDto) {
        if (!taskWorkspaceId.equalsIgnoreCase(projectDto.getWorkspaceId())) {
            throw new NoAccessException();
        }
    }

    private void validateTaskTeamWithinProjectTeams(String taskTeamId, ProjectDto projectDto) {
        boolean hasTaskTeamIsInProjectTeams = projectDto
                .getProjectTeams()
                .stream()
                .reduce(false, (acc, curr) -> curr.getTeamId().equals(taskTeamId), (b1, b2) -> b1 || b2);
        log.info("Validate task team within project teams: {}, taskTeamId: {}", hasTaskTeamIsInProjectTeams, taskTeamId);
        if (!hasTaskTeamIsInProjectTeams) {
            throw new BusinessException("project.assign-task.task-team-not-in-project-teams");
        }
    }
}
