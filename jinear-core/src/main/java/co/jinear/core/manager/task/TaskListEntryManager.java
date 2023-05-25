package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskListEntryInitializeRequestConverter;
import co.jinear.core.model.dto.task.TaskListEntryDto;
import co.jinear.core.model.request.task.TaskListEntryInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.task.InitializeTaskListEntryVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.list.entry.TaskListEntryOperationService;
import co.jinear.core.service.task.list.entry.TaskListEntryRetrieveService;
import co.jinear.core.validator.task.TaskListAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListEntryManager {

    private final TaskListAccessValidator taskListAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TaskListEntryOperationService taskListEntryOperationService;
    private final TaskListEntryRetrieveService taskListEntryRetrieveService;
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
        String passiveId = taskListEntryOperationService.deleteEntry(taskListEntryId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    private void validateAccess(String taskListEntryId, String currentAccountId) {
        TaskListEntryDto taskListEntryDto = taskListEntryRetrieveService.retrieve(taskListEntryId);
        taskListAccessValidator.validateHasTaskListAccess(taskListEntryDto.getTaskListId(), currentAccountId);
    }
}
