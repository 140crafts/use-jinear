package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskListInitializeRequestConverter;
import co.jinear.core.converter.task.TaskListUpdateRequestConverter;
import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.model.request.task.*;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskListResponse;
import co.jinear.core.model.vo.task.InitializeTaskListVo;
import co.jinear.core.model.vo.task.UpdateTaskListDueDateVo;
import co.jinear.core.model.vo.task.UpdateTaskListStateVo;
import co.jinear.core.model.vo.task.UpdateTaskListTitleVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.list.TaskListOperationService;
import co.jinear.core.validator.task.TaskListAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListManager {

    private final TaskListOperationService taskListOperationService;
    private final TaskListAccessValidator taskListAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskListInitializeRequestConverter taskListInitializeRequestConverter;
    private final TaskListUpdateRequestConverter taskListUpdateRequestConverter;

    public TaskListResponse initializeTaskList(TaskListInitializeRequest taskListInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, taskListInitializeRequest.getTeamId());
        log.info("Initialize task list has started. currentAccountId: {}", currentAccountId);
        InitializeTaskListVo initializeTaskListVo = taskListInitializeRequestConverter.convert(taskListInitializeRequest, currentAccountId);
        TaskListDto taskListDto = taskListOperationService.initializeTaskList(initializeTaskListVo);
        return mapResponse(taskListDto);
    }

    public BaseResponse updateDueDate(TaskListUpdateDueDateRequest taskListInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasTaskListAccess(taskListInitializeRequest, currentAccountId);
        log.info("Update task list date has started. currentAccountId: {}", currentAccountId);
        UpdateTaskListDueDateVo updateTaskListDueDateVo = taskListUpdateRequestConverter.convert(taskListInitializeRequest);
        taskListOperationService.updateDueDate(updateTaskListDueDateVo);
        return new BaseResponse();
    }

    public BaseResponse updateTitle(TaskListUpdateTitleRequest taskListUpdateTitleRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasTaskListAccess(taskListUpdateTitleRequest, currentAccountId);
        log.info("Update task list title has started. currentAccountId: {}", currentAccountId);
        UpdateTaskListTitleVo updateTaskListTitleVo = taskListUpdateRequestConverter.convert(taskListUpdateTitleRequest);
        taskListOperationService.updateTitle(updateTaskListTitleVo);
        return new BaseResponse();
    }

    public BaseResponse updateState(TaskListUpdateStateRequest taskListUpdateStateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasTaskListAccess(taskListUpdateStateRequest, currentAccountId);
        log.info("Update task list state has started. currentAccountId: {}", currentAccountId);
        UpdateTaskListStateVo updateTaskListStateVo = taskListUpdateRequestConverter.convert(taskListUpdateStateRequest);
        taskListOperationService.updateState(updateTaskListStateVo);
        return new BaseResponse();
    }

    private void validateHasTaskListAccess(TaskListUpdateRequest request, String currentAccountId) {
        taskListAccessValidator.validateHasTaskListAccess(request.getTaskListId(), currentAccountId);
    }

    private TaskListResponse mapResponse(TaskListDto taskListDto) {
        TaskListResponse taskListResponse = new TaskListResponse();
        taskListResponse.setTaskListDto(taskListDto);
        return taskListResponse;
    }
}
