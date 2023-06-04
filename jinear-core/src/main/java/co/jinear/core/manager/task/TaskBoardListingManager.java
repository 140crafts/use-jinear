package co.jinear.core.manager.task;


import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.response.task.TaskBoardListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskBoardRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.board.TaskBoardListingService;
import co.jinear.core.service.task.board.TaskBoardRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardListingManager {

    private final TeamAccessValidator teamAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TaskBoardListingService taskBoardListingService;
    private final TaskBoardRetrieveService taskBoardRetrieveService;

    public TaskBoardRetrieveResponse retrieve(String taskBoardId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskBoardDto taskBoardDto = taskBoardRetrieveService.retrieve(taskBoardId);
        teamAccessValidator.validateTeamAccess(currentAccountId, taskBoardDto.getTeamId());
        log.info("Retrieve task board has started. currentAccountId: {}", currentAccountId);
        return mapResponse(taskBoardDto);
    }

    public TaskBoardListingPaginatedResponse retrieveAll(String workspaceId, String teamId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve all task boards has started. currentAccountId: {}", currentAccountId);
        Page<TaskBoardDto> results = taskBoardListingService.retrieveTaskBoards(workspaceId, teamId, page);
        return mapResponse(results);
    }

    private TaskBoardRetrieveResponse mapResponse(TaskBoardDto taskBoardDto) {
        TaskBoardRetrieveResponse response = new TaskBoardRetrieveResponse();
        response.setTaskBoardDto(taskBoardDto);
        return response;
    }

    private TaskBoardListingPaginatedResponse mapResponse(Page<TaskBoardDto> results) {
        PageDto<TaskBoardDto> pageDto = new PageDto<>(results);
        TaskBoardListingPaginatedResponse taskBoardListingPaginatedResponse = new TaskBoardListingPaginatedResponse();
        taskBoardListingPaginatedResponse.setTaskListDetailedDtoPageDto(pageDto);
        return taskBoardListingPaginatedResponse;
    }
}
