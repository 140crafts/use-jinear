package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskBoardEntryFilterRequestToTaskSearchFilterVoConverter;
import co.jinear.core.converter.task.TaskBoardEntryInitializeRequestConverter;
import co.jinear.core.converter.task.TaskDtoToTaskBoardEntryDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.PlainTaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import co.jinear.core.model.request.task.TaskBoardEntryFilterRequest;
import co.jinear.core.model.request.task.TaskBoardEntryInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskBoardEntryPaginatedResponse;
import co.jinear.core.model.vo.task.InitializeTaskBoardEntryVo;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.board.TaskBoardRetrieveService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryListingService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryOperationService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardEntryManager {

    private final TaskBoardAccessValidator taskBoardAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TaskBoardEntryOperationService taskBoardEntryOperationService;
    private final TaskBoardEntryRetrieveService taskBoardEntryRetrieveService;
    private final TaskBoardEntryListingService taskBoardEntryListingService;
    private final TaskBoardEntryInitializeRequestConverter taskBoardEntryInitializeRequestConverter;
    private final PassiveService passiveService;
    private final TaskBoardRetrieveService taskBoardRetrieveService;
    private final TaskActivityService taskActivityService;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskBoardEntryFilterRequestToTaskSearchFilterVoConverter taskBoardEntryFilterRequestToTaskSearchFilterVoConverter;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TaskListingService taskListingService;
    private final TaskDtoToTaskBoardEntryDtoConverter taskDtoToTaskBoardEntryDtoConverter;

    public BaseResponse initializeTaskBoardEntry(TaskBoardEntryInitializeRequest taskBoardInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskBoardDto taskBoardDto = taskBoardRetrieveService.retrieve(taskBoardInitializeRequest.getTaskBoardId());
        validateBoardStatus(taskBoardDto.getState());
        taskBoardAccessValidator.validateHasTaskBoardAccess(taskBoardDto, currentAccountId);
        InitializeTaskBoardEntryVo initializeTaskBoardEntryVo = taskBoardEntryInitializeRequestConverter.convert(taskBoardInitializeRequest);
        TaskBoardEntryDto boardEntryDto = taskBoardEntryOperationService.initialize(initializeTaskBoardEntryVo);
        initializeWorkspaceActivity(currentAccountId, currentAccountSessionId, boardEntryDto);
        return new BaseResponse();
    }

    public BaseResponse deleteTaskBoardEntry(String taskBoardEntryId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        validateAccess(taskBoardEntryId, currentAccountId);
        validateBoardStatus(taskBoardEntryId);
        log.info("Delete task board entry has started. currentAccountId: {}", currentAccountId);
        TaskBoardEntryDto entryDto = taskBoardEntryOperationService.deleteEntry(taskBoardEntryId);
        passiveService.assignOwnership(entryDto.getPassiveId(), currentAccountId);
        taskActivityService.initializeTaskRemovedFromTaskBoardActivity(currentAccountId, currentAccountSessionId, entryDto);
        return new BaseResponse();
    }

    public BaseResponse changeOrder(String taskBoardEntryId, Integer newOrder) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        validateAccess(taskBoardEntryId, currentAccountId);
        validateBoardStatus(taskBoardEntryId);
        log.info("Change task board entry order has started. currentAccountId: {}", currentAccountId);
        TaskBoardEntryDto boardEntryDto = taskBoardEntryOperationService.changeOrder(taskBoardEntryId, newOrder);
        taskActivityService.initializeTaskOrderChangedOnTaskBoardActivity(currentAccountId, currentAccountSessionId, boardEntryDto);
        return new BaseResponse();
    }

    public BaseResponse changeFilteredOrder(String taskBoardEntryId, String taskBoardEntryIdBefore, String taskBoardEntryIdAfter) {
        validateRequest(taskBoardEntryIdBefore, taskBoardEntryIdAfter);
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        validateAccess(taskBoardEntryId, currentAccountId);
        validateBoardStatus(taskBoardEntryId);
        log.info("Change task board entry order has started. currentAccountId: {}", currentAccountId);
        TaskBoardEntryDto boardEntryDto = taskBoardEntryOperationService.changeFilteredOrder(taskBoardEntryId, taskBoardEntryIdBefore, taskBoardEntryIdAfter);
        taskActivityService.initializeTaskOrderChangedOnTaskBoardActivity(currentAccountId, currentAccountSessionId, boardEntryDto);
        return new BaseResponse();
    }

    public TaskBoardEntryPaginatedResponse retrieveFromTaskBoard(String taskBoardId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskBoardAccessValidator.validateHasTaskBoardAccess(taskBoardId, currentAccountId);
        log.info("Retrieve task board entries from task board has started. currentAccountId: {}", currentAccountId);
        Page<TaskBoardEntryDto> results = taskBoardEntryListingService.retrieveTaskBoardEntries(taskBoardId, page);
        return mapResults(results);
    }

    public TaskBoardEntryPaginatedResponse filterFromTaskBoard(String taskBoardId, TaskBoardEntryFilterRequest taskBoardEntryFilterRequest, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskBoardDto taskBoardDto = taskBoardRetrieveService.retrieve(taskBoardId);
        taskBoardAccessValidator.validateHasTaskBoardAccess(taskBoardDto, currentAccountId);
        log.info("Filter task board entries from task board has started. currentAccountId: {}", currentAccountId);
        TeamMemberDto teamMemberDto = teamMemberRetrieveService.retrieveMembership(taskBoardDto.getWorkspaceId(), taskBoardDto.getTeamId(), currentAccountId).orElseThrow();
        TaskSearchFilterVo taskSearchFilterVo = taskBoardEntryFilterRequestToTaskSearchFilterVoConverter.convert(taskBoardId, taskBoardEntryFilterRequest, page, List.of(teamMemberDto));
        Page<TaskDto> taskDtoPage = taskListingService.filterTasks(taskSearchFilterVo);
        Page<TaskBoardEntryDto> results = taskDtoToTaskBoardEntryDtoConverter.convert(taskDtoPage, taskBoardId);
        return mapResults(results);
    }

    private void initializeWorkspaceActivity(String currentAccountId, String currentAccountSessionId, TaskBoardEntryDto boardEntryDto) {
        TaskDto taskDto = taskRetrieveService.retrievePlain(boardEntryDto.getTaskId());
        taskActivityService.initializeTaskAddedToTaskBoardActivity(currentAccountId, currentAccountSessionId, taskDto, boardEntryDto.getTaskBoardId());
    }

    private void validateBoardStatus(String taskBoardEntryId) {
        PlainTaskBoardEntryDto plainTaskBoardEntryDto = taskBoardEntryRetrieveService.retrievePlain(taskBoardEntryId);
        validateBoardStatus(plainTaskBoardEntryDto.getTaskBoard().getState());
    }

    private void validateBoardStatus(TaskBoardStateType state) {
        if (TaskBoardStateType.CLOSED.equals(state)) {
            throw new BusinessException("task-board.entry.board-closed");
        }
    }

    private void validateAccess(String taskBoardEntryId, String currentAccountId) {
        TaskBoardEntryDto taskBoardEntryDto = taskBoardEntryRetrieveService.retrieve(taskBoardEntryId);
        taskBoardAccessValidator.validateHasTaskBoardAccess(taskBoardEntryDto.getTaskBoardId(), currentAccountId);
    }

    private TaskBoardEntryPaginatedResponse mapResults(Page<TaskBoardEntryDto> results) {
        PageDto<TaskBoardEntryDto> pageDto = new PageDto<>(results);
        TaskBoardEntryPaginatedResponse taskBoardEntryPaginatedResponse = new TaskBoardEntryPaginatedResponse();
        taskBoardEntryPaginatedResponse.setTaskListEntryDtoPageDto(pageDto);
        return taskBoardEntryPaginatedResponse;
    }

    private void validateRequest(String taskBoardEntryIdBefore, String taskBoardEntryIdAfter) {
        if (StringUtils.isBlank(taskBoardEntryIdBefore) && StringUtils.isBlank(taskBoardEntryIdAfter)) {
            throw new NotValidException();
        }
    }
}
