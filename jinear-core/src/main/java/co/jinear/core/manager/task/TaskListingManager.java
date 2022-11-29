package co.jinear.core.manager.task;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskRetrieveAllRequest;
import co.jinear.core.model.response.task.TaskListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskListingService taskListingService;

    public TaskListingResponse retrieveAllTasks(String workspaceId, String teamId, int page) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, workspaceId);
        validateTeamAccess(currentAccount, workspaceId, teamId);
        log.info("Retrieve all tasks has started. currentAccount: {}", currentAccount);
        Page<TaskDto> taskDtoPage = taskListingService.retrieveAllTasksFromWorkspaceAndTeam(workspaceId, teamId, page);
        return mapResponse(taskDtoPage);
    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private void validateTeamAccess(String currentAccount, String workspaceId, String teamId) {
        teamAccessValidator.validateTeamAccess(currentAccount, workspaceId, teamId);
    }

    private TaskListingResponse mapResponse(Page<TaskDto> taskDtoPage) {
        TaskListingResponse taskListingResponse = new TaskListingResponse();
        taskListingResponse.setTaskDtoPage(new PageDto<>(taskDtoPage));
        return taskListingResponse;
    }
}
