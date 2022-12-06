package co.jinear.core.manager.task;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskRetrieveIntersectingRequest;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskListingResponse;
import co.jinear.core.model.vo.task.SearchIntersectingTasksVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskListingService taskListingService;
    private final ModelMapper modelMapper;

    public TaskListingPaginatedResponse retrieveAllTasks(String workspaceId, String teamId, int page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        validateTeamAccess(currentAccount, workspaceId, teamId);
        log.info("Retrieve all tasks has started. currentAccount: {}", currentAccount);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksFromWorkspaceAndTeam(workspaceId, teamId, page);
        return mapResponse(taskDtoPage);
    }

    public TaskListingResponse retrieveAllIntersectingTasks(TaskRetrieveIntersectingRequest taskRetrieveIntersectingRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, taskRetrieveIntersectingRequest.getWorkspaceId());
        validateTeamAccess(currentAccount, taskRetrieveIntersectingRequest.getWorkspaceId(), taskRetrieveIntersectingRequest.getTeamId());
        log.info("Retrieve all tasks intersecting has started. currentAccount: {}", currentAccount);
        SearchIntersectingTasksVo searchIntersectingTasksVo = modelMapper.map(taskRetrieveIntersectingRequest, SearchIntersectingTasksVo.class);
        List<TaskDto> result = taskListingService.retrieveAllIntersectingTasks(searchIntersectingTasksVo);
        return mapResponse(result);
    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private void validateTeamAccess(String currentAccount, String workspaceId, String teamId) {
        teamAccessValidator.validateTeamAccess(currentAccount, workspaceId, teamId);
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
