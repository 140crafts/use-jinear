package co.jinear.core.manager.task;


import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.*;
import co.jinear.core.model.response.task.TaskAndTaskBoardRelationResponse;
import co.jinear.core.model.response.task.TaskBoardListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskBoardRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.board.TaskBoardListingService;
import co.jinear.core.service.task.board.TaskBoardRetrieveService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryListingService;
import co.jinear.core.validator.task.TaskAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardListingManager {

    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TaskBoardListingService taskBoardListingService;
    private final TaskBoardRetrieveService taskBoardRetrieveService;
    private final TaskAccessValidator taskAccessValidator;
    private final TaskBoardEntryListingService taskBoardEntryListingService;
    private final TaskRetrieveService taskRetrieveService;

    public TaskBoardRetrieveResponse retrieve(String taskBoardId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskBoardDto taskBoardDto = taskBoardRetrieveService.retrieve(taskBoardId);
        teamAccessValidator.validateTeamAccess(currentAccountId, taskBoardDto.getTeamId());
        log.info("Retrieve task board has started. currentAccountId: {}", currentAccountId);
        return mapResponse(taskBoardDto);
    }

    public TaskBoardListingPaginatedResponse retrieveAllByTeam(String workspaceId, String teamId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve all task boards has started. currentAccountId: {}", currentAccountId);
        Page<TaskBoardDto> results = taskBoardListingService.retrieveTaskBoards(workspaceId, teamId, page);
        return mapResponse(results);
    }

    public TaskBoardListingPaginatedResponse filterAllByName(String workspaceId, String filterRecentsByName, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve all task boards has started. currentAccountId: {}", currentAccountId);
        Page<TaskBoardDto> results = taskBoardListingService.retrieveTaskBoardsFilterByName(workspaceId, filterRecentsByName, page);
        return mapResponse(results);
    }

    public TaskBoardListingPaginatedResponse filterAllByTeamAndName(String workspaceId, String teamId, String filterRecentsByName, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve all task boards has started. currentAccountId: {}", currentAccountId);
        Page<TaskBoardDto> results = taskBoardListingService.retrieveTaskBoardsFilterByTeamAndName(workspaceId, teamId, filterRecentsByName, page);
        return mapResponse(results);
    }

    public TaskAndTaskBoardRelationResponse retrieveTasksBoardAndRecentBoards(String taskId, String filterRecentsByName) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Retrieve tasks board and recent boards has started. currentAccountId: {}", currentAccountId);
        List<TaskBoardEntryDetailedDto> taskAlreadyInTheseBoards = retrieveTasksBoards(taskId);
        PageDto<TaskBoardDto> recentBoardsPageDto = retrieveRecentBoardsTaskNotInAndFilterByName(taskDto, taskAlreadyInTheseBoards, filterRecentsByName);
        TaskAndTaskBoardRelationDto taskAndTaskBoardRelation = mapTaskAndTaskBoardRelationDto(taskAlreadyInTheseBoards, recentBoardsPageDto);
        return mapResponse(taskAndTaskBoardRelation);
    }

    private List<TaskBoardEntryDetailedDto> retrieveTasksBoards(String taskId) {
        return taskBoardEntryListingService.retrieveAllEntriesRelatedWithTask(taskId);
    }

    private PageDto<TaskBoardDto> retrieveRecentBoardsTaskNotInAndFilterByName(TaskDto taskDto, List<TaskBoardEntryDetailedDto> taskAlreadyInTheseBoards, String filterRecentsByName) {
        List<String> taskAlreadyInTheseBoardsIds = taskAlreadyInTheseBoards
                .stream()
                .map(TaskBoardEntryDto::getTaskBoardId)
                .toList();

        Page<TaskBoardDto> recentBoardsPage = Optional.ofNullable(filterRecentsByName)
                .filter(StringUtils::isNotBlank)
                .map(name -> taskBoardListingService.retrieveTaskBoardsExcludingSomeAndFilterByName(taskAlreadyInTheseBoardsIds, taskDto.getWorkspaceId(), taskDto.getTeamId(), filterRecentsByName, 0))
                .orElseGet(() -> Optional.of(taskAlreadyInTheseBoardsIds)
                        .filter(boardIds -> Boolean.FALSE.equals(boardIds.isEmpty()))
                        .map(boardIds -> taskBoardListingService.retrieveTaskBoardsExcludingSome(taskAlreadyInTheseBoardsIds, taskDto.getWorkspaceId(), taskDto.getTeamId(), 0))
                        .orElseGet(() -> taskBoardListingService.retrieveTaskBoards(taskDto.getWorkspaceId(), taskDto.getTeamId(), 0))
                );

        return new PageDto<>(recentBoardsPage);
    }

    private TaskAndTaskBoardRelationDto mapTaskAndTaskBoardRelationDto(List<TaskBoardEntryDetailedDto> taskAlreadyInTheseBoards, PageDto<TaskBoardDto> recentBoardsPageDto) {
        TaskAndTaskBoardRelationDto taskAndTaskBoardRelation = new TaskAndTaskBoardRelationDto();
        taskAndTaskBoardRelation.setRecentBoards(recentBoardsPageDto);
        taskAndTaskBoardRelation.setAlreadyAddedBoards(taskAlreadyInTheseBoards);
        return taskAndTaskBoardRelation;
    }

    private TaskAndTaskBoardRelationResponse mapResponse(TaskAndTaskBoardRelationDto taskAndTaskBoardRelation) {
        TaskAndTaskBoardRelationResponse taskAndTaskBoardRelationResponse = new TaskAndTaskBoardRelationResponse();
        taskAndTaskBoardRelationResponse.setTaskAndTaskBoardRelation(taskAndTaskBoardRelation);
        return taskAndTaskBoardRelationResponse;
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
