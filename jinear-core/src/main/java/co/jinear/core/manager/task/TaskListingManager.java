package co.jinear.core.manager.task;

import co.jinear.core.converter.task.SearchIntersectingTasksVoConverter;
import co.jinear.core.converter.task.TaskFilterRequestConverter;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.request.task.RetrieveIntersectingTasksFromTeamRequest;
import co.jinear.core.model.request.task.RetrieveIntersectingTasksFromWorkspaceRequest;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskListingResponse;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromTeamVo;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromWorkspaceVo;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskListingService taskListingService;
    private final SearchIntersectingTasksVoConverter searchIntersectingTasksVoConverter;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TopicRetrieveService topicRetrieveService;
    private final TaskFilterRequestConverter taskFilterRequestConverter;

    public TaskListingPaginatedResponse filterTasks(TaskFilterRequest taskFilterRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, taskFilterRequest.getWorkspaceId());
        validateTeamAccess(currentAccount, taskFilterRequest);
        TaskSearchFilterVo taskSearchFilterVo = taskFilterRequestConverter.convert(taskFilterRequest);
        Page<TaskDto> taskDtoPage = taskListingService.filterTasks(taskSearchFilterVo);
        return mapResponse(taskDtoPage);
    }

    public TaskListingPaginatedResponse retrieveAllTasks(String workspaceId, String teamId, int page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        validateTeamAccess(currentAccount, workspaceId, teamId);
        log.info("Retrieve all tasks has started. currentAccount: {}", currentAccount);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksFromWorkspaceAndTeam(workspaceId, teamId, page);
        return mapResponse(taskDtoPage);
    }

    public TaskListingResponse retrieveAllIntersectingTasks(RetrieveIntersectingTasksFromWorkspaceRequest retrieveIntersectingTasksFromWorkspaceRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, retrieveIntersectingTasksFromWorkspaceRequest.getWorkspaceId());
        log.info("Retrieve all tasks intersecting has started. currentAccount: {}", currentAccount);
        List<String> accountTeamIds = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, retrieveIntersectingTasksFromWorkspaceRequest.getWorkspaceId())
                .stream()
                .map(TeamMemberDto::getTeamId)
                .toList();
        SearchIntersectingTasksFromWorkspaceVo searchIntersectingTasksFromWorkspaceVo = searchIntersectingTasksVoConverter.map(retrieveIntersectingTasksFromWorkspaceRequest, accountTeamIds);
        List<TaskDto> result = taskListingService.retrieveAllIntersectingTasksFromTeamList(searchIntersectingTasksFromWorkspaceVo);
        return mapResponse(result);
    }

    public TaskListingResponse retrieveAllIntersectingTasksFromTeam(RetrieveIntersectingTasksFromTeamRequest retrieveIntersectingTasksFromTeamRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, retrieveIntersectingTasksFromTeamRequest.getWorkspaceId());
        validateTeamAccess(currentAccount, retrieveIntersectingTasksFromTeamRequest.getWorkspaceId(), retrieveIntersectingTasksFromTeamRequest.getTeamId());
        log.info("Retrieve all tasks from team intersecting has started. currentAccount: {}", currentAccount);
        SearchIntersectingTasksFromTeamVo searchIntersectingTasksFromTeamVo = searchIntersectingTasksVoConverter.map(retrieveIntersectingTasksFromTeamRequest);
        List<TaskDto> result = taskListingService.retrieveAllIntersectingTasksFromTeam(searchIntersectingTasksFromTeamVo);
        return mapResponse(result);
    }

    public TaskListingPaginatedResponse retrieveFromWorkflowStatus(String workspaceId, String teamId, String workflowStatusId, Integer page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        validateTeamAccess(currentAccount, workspaceId, teamId);
        log.info("Retrieve tasks from workflow status has started. currentAccount: {}", currentAccount);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksFromWorkspaceAndTeamWithWorkflowStatus(workspaceId, teamId, workflowStatusId, page);
        return mapResponse(taskDtoPage);
    }

    public TaskListingPaginatedResponse retrieveFromTopic(String workspaceId, String teamId, String topicTag, Integer page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        validateTeamAccess(currentAccount, workspaceId, teamId);
        log.info("Retrieve tasks from topic has started. currentAccount: {}", currentAccount);
        TopicDto topicDto = topicRetrieveService.retrieveByTag(teamId, workspaceId, topicTag);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksFromWorkspaceAndTeamWithTopic(workspaceId, teamId, topicDto.getTopicId(), page);
        return mapResponse(taskDtoPage);
    }

    public TaskListingPaginatedResponse retrieveWithAssignee(String workspaceId, String teamId, String assigneeId, Integer page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        validateTeamAccess(currentAccount, workspaceId, teamId);
        log.info("Retrieve tasks with assignee has started. currentAccount: {}", currentAccount);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksWithAssignee(workspaceId, teamId, assigneeId, page);
        return mapResponse(taskDtoPage);
    }

    public TaskListingPaginatedResponse retrieveTasksAssignedToCurrentAccount(String workspaceId, Integer page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        log.info("Retrieve tasks assigneed to current account has started. currentAccount: {}", currentAccount);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksWithAssignee(workspaceId, currentAccount, page);
        return mapResponse(taskDtoPage);
    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private void validateTeamAccess(String currentAccount, String workspaceId, String teamId) {
        teamAccessValidator.validateTeamAccess(currentAccount, workspaceId, teamId);
    }

    private void validateTeamAccess(String currentAccount, TaskFilterRequest taskFilterRequest) {
        List<String> teamIdList = taskFilterRequest.getTeamIdList();
        if (Objects.isNull(teamIdList) || teamIdList.isEmpty()) {
            throw new NotValidException();
        }
        boolean anyTeamIdIsEmpty = teamIdList.stream().anyMatch(Strings::isBlank);
        if (anyTeamIdIsEmpty) {
            throw new NotValidException();
        }
        Optional.of(taskFilterRequest)
                .map(TaskFilterRequest::getTeamIdList)
                .map(Collection::stream)
                .ifPresent(teamIdStream -> teamIdStream.forEach(teamId -> teamAccessValidator.validateTeamAccess(currentAccount, teamId)));

    }

    private TaskListingPaginatedResponse mapResponse(Page<TaskDto> taskDtoPage) {
        TaskListingPaginatedResponse taskListingPaginatedResponse = new TaskListingPaginatedResponse();
        taskListingPaginatedResponse.setTaskDtoPage(new PageDto<>(taskDtoPage));
        return taskListingPaginatedResponse;
    }

    private TaskListingResponse mapResponse(List<TaskDto> result) {
        TaskListingResponse taskListingResponse = new TaskListingResponse();
        taskListingResponse.setTaskDtoList(result);
        return taskListingResponse;
    }
}
