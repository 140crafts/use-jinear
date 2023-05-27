package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskListEntryInitializeRequestConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskListEntryDto;
import co.jinear.core.model.request.task.TaskListEntryInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskListEntryPaginatedResponse;
import co.jinear.core.model.vo.task.InitializeTaskListEntryVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.list.entry.TaskListEntryListingService;
import co.jinear.core.service.task.list.entry.TaskListEntryOperationService;
import co.jinear.core.service.task.list.entry.TaskListEntryRetrieveService;
import co.jinear.core.validator.task.TaskListAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListEntryManager {

    private final TaskListAccessValidator taskListAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TaskListEntryOperationService taskListEntryOperationService;
    private final TaskListEntryRetrieveService taskListEntryRetrieveService;
    private final TaskListEntryListingService taskListEntryListingService;
    private final TaskListEntryInitializeRequestConverter taskListEntryInitializeRequestConverter;
    private final PassiveService passiveService;

    public BaseResponse initializeTaskListEntry(TaskListEntryInitializeRequest taskListInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskListAccessValidator.validateHasTaskListAccess(taskListInitializeRequest.getTaskListId(), currentAccountId);
        InitializeTaskListEntryVo initializeTaskListEntryVo = taskListEntryInitializeRequestConverter.convert(taskListInitializeRequest);
        taskListEntryOperationService.initialize(initializeTaskListEntryVo);
        return new BaseResponse();
    }

    public BaseResponse deleteTaskListEntry(String taskListEntryId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskListEntryId, currentAccountId);
        log.info("Delete task list entry has started. currentAccountId: {}", currentAccountId);
        String passiveId = taskListEntryOperationService.deleteEntry(taskListEntryId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse changeOrder(String taskListEntryId, Integer newOrder) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskListEntryId, currentAccountId);
        log.info("Change task list entry order has started. currentAccountId: {}", currentAccountId);
        taskListEntryOperationService.changeOrder(taskListEntryId, newOrder);
        return new BaseResponse();
    }

    public TaskListEntryPaginatedResponse retrieveFromTaskList(String taskListId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskListAccessValidator.validateHasTaskListAccess(taskListId, currentAccountId);
        log.info("Retrieve task list entries from task list has started. currentAccountId: {}", currentAccountId);
        Page<TaskListEntryDto> results = taskListEntryListingService.retrieveTaskListEntries(taskListId, page);
        return mapResults(results);
    }

    private void validateAccess(String taskListEntryId, String currentAccountId) {
        TaskListEntryDto taskListEntryDto = taskListEntryRetrieveService.retrieve(taskListEntryId);
        taskListAccessValidator.validateHasTaskListAccess(taskListEntryDto.getTaskListId(), currentAccountId);
    }

    private TaskListEntryPaginatedResponse mapResults(Page<TaskListEntryDto> results) {
        PageDto<TaskListEntryDto> pageDto = new PageDto<>(results);
        TaskListEntryPaginatedResponse taskListEntryPaginatedResponse = new TaskListEntryPaginatedResponse();
        taskListEntryPaginatedResponse.setTaskListEntryDtoPageDto(pageDto);
        return taskListEntryPaginatedResponse;
    }
}
