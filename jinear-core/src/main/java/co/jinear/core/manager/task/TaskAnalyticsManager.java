package co.jinear.core.manager.task;

import co.jinear.core.model.dto.task.TaskAnalyticNumbersDto;
import co.jinear.core.model.response.task.TaskNumbersResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskAnalyticsService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAnalyticsManager {

    private final TaskAnalyticsService taskAnalyticsService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;

    public TaskNumbersResponse retrieveTaskNumbers(String workspaceId, String teamId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve task numbers has started. workspaceId: {}, teamId: {}", workspaceId, teamId);
        validateAccess(workspaceId, teamId, currentAccountId);
        TaskAnalyticNumbersDto taskAnalyticNumbersDto = taskAnalyticsService.retrieveNumbers(workspaceId,teamId, ZonedDateTime.now());
        return mapResponse(taskAnalyticNumbersDto);
    }

    private void validateAccess(String workspaceId, String teamId, String currentAccountId) {
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
    }

    private TaskNumbersResponse mapResponse(TaskAnalyticNumbersDto taskAnalyticNumbersDto) {
        TaskNumbersResponse taskNumbersResponse = new TaskNumbersResponse();
        taskNumbersResponse.setTaskAnalyticNumbersDto(taskAnalyticNumbersDto);
        return taskNumbersResponse;
    }
}
