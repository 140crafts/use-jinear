package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskBoardEntryInitializeRequestConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import co.jinear.core.model.request.task.TaskBoardEntryInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskBoardEntryPaginatedResponse;
import co.jinear.core.model.vo.task.InitializeTaskBoardEntryVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.board.TaskBoardRetrieveService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryListingService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryOperationService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryRetrieveService;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

    public BaseResponse initializeTaskBoardEntry(TaskBoardEntryInitializeRequest taskBoardInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskBoardDto taskBoardDto = taskBoardRetrieveService.retrieve(taskBoardInitializeRequest.getTaskBoardId());
        validateBoardStatus(taskBoardDto);
        taskBoardAccessValidator.validateHasTaskBoardAccess(taskBoardDto, currentAccountId);
        InitializeTaskBoardEntryVo initializeTaskBoardEntryVo = taskBoardEntryInitializeRequestConverter.convert(taskBoardInitializeRequest);
        TaskBoardEntryDto boardEntryDto = taskBoardEntryOperationService.initialize(initializeTaskBoardEntryVo);
        initializeWorkspaceActivity(currentAccountId, boardEntryDto);
        return new BaseResponse();
    }

    public BaseResponse deleteTaskBoardEntry(String taskBoardEntryId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskBoardEntryId, currentAccountId);
        log.info("Delete task board entry has started. currentAccountId: {}", currentAccountId);
        TaskBoardEntryDto entryDto = taskBoardEntryOperationService.deleteEntry(taskBoardEntryId);
        passiveService.assignOwnership(entryDto.getPassiveId(), currentAccountId);
        taskActivityService.initializeTaskRemovedFromTaskBoardActivity(currentAccountId, entryDto);
        return new BaseResponse();
    }

    public BaseResponse changeOrder(String taskBoardEntryId, Integer newOrder) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskBoardEntryId, currentAccountId);
        log.info("Change task board entry order has started. currentAccountId: {}", currentAccountId);
        TaskBoardEntryDto boardEntryDto = taskBoardEntryOperationService.changeOrder(taskBoardEntryId, newOrder);
        taskActivityService.initializeTaskOrderChangedOnTaskBoardActivity(currentAccountId, boardEntryDto);
        return new BaseResponse();
    }

    public TaskBoardEntryPaginatedResponse retrieveFromTaskBoard(String taskBoardId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskBoardAccessValidator.validateHasTaskBoardAccess(taskBoardId, currentAccountId);
        log.info("Retrieve task board entries from task board has started. currentAccountId: {}", currentAccountId);
        Page<TaskBoardEntryDto> results = taskBoardEntryListingService.retrieveTaskBoardEntries(taskBoardId, page);
        return mapResults(results);
    }

    private void initializeWorkspaceActivity(String currentAccountId, TaskBoardEntryDto boardEntryDto) {
        TaskDto taskDto = taskRetrieveService.retrievePlain(boardEntryDto.getTaskId());
        taskActivityService.initializeTaskAddedToTaskBoardActivity(currentAccountId, taskDto, boardEntryDto.getTaskBoardId());
    }

    private void validateBoardStatus(TaskBoardDto taskBoardDto) {
        if (TaskBoardStateType.CLOSED.equals(taskBoardDto.getState())) {
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
}
