package co.jinear.core.manager.task;


import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.model.response.task.TaskListListingPaginatedResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.list.TaskListListingService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListListingManager {

    private final TeamAccessValidator teamAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TaskListListingService taskListListingService;

    public TaskListListingPaginatedResponse retrieveAll(String workspaceId, String teamId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve all task lists has started. currentAccountId: {}", currentAccountId);
        Page<TaskListDto> results = taskListListingService.retrieveTaskLists(workspaceId, teamId, page);
        return mapResponse(results);
    }

    private TaskListListingPaginatedResponse mapResponse(Page<TaskListDto> results) {
        PageDto<TaskListDto> pageDto = new PageDto<>(results);
        TaskListListingPaginatedResponse taskListListingPaginatedResponse = new TaskListListingPaginatedResponse();
        taskListListingPaginatedResponse.setTaskListDetailedDtoPageDto(pageDto);
        return taskListListingPaginatedResponse;
    }
}
