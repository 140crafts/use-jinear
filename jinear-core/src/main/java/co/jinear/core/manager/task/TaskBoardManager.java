package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskBoardInitializeRequestConverter;
import co.jinear.core.converter.task.TaskBoardUpdateRequestConverter;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.request.task.*;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskBoardResponse;
import co.jinear.core.model.vo.task.InitializeTaskBoardVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardDueDateVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardStateVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardTitleVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.board.TaskBoardOperationService;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardManager {

    private final TaskBoardOperationService taskBoardOperationService;
    private final TaskBoardAccessValidator taskBoardAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskBoardInitializeRequestConverter taskBoardInitializeRequestConverter;
    private final TaskBoardUpdateRequestConverter taskBoardUpdateRequestConverter;

    public TaskBoardResponse initializeTaskBoard(TaskBoardInitializeRequest taskBoardInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, taskBoardInitializeRequest.getTeamId());
        log.info("Initialize task board has started. currentAccountId: {}", currentAccountId);
        InitializeTaskBoardVo initializeTaskBoardVo = taskBoardInitializeRequestConverter.convert(taskBoardInitializeRequest, currentAccountId);
        TaskBoardDto taskBoardDto = taskBoardOperationService.initializeTaskBoard(initializeTaskBoardVo);
        return mapResponse(taskBoardDto);
    }

    public BaseResponse updateDueDate(TaskBoardUpdateDueDateRequest taskBoardInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasTaskBoardAccess(taskBoardInitializeRequest, currentAccountId);
        log.info("Update task board date has started. currentAccountId: {}", currentAccountId);
        UpdateTaskBoardDueDateVo updateTaskBoardDueDateVo = taskBoardUpdateRequestConverter.convert(taskBoardInitializeRequest);
        taskBoardOperationService.updateDueDate(updateTaskBoardDueDateVo);
        return new BaseResponse();
    }

    public BaseResponse updateTitle(TaskBoardUpdateTitleRequest taskBoardUpdateTitleRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasTaskBoardAccess(taskBoardUpdateTitleRequest, currentAccountId);
        log.info("Update task board title has started. currentAccountId: {}", currentAccountId);
        UpdateTaskBoardTitleVo updateTaskBoardTitleVo = taskBoardUpdateRequestConverter.convert(taskBoardUpdateTitleRequest);
        taskBoardOperationService.updateTitle(updateTaskBoardTitleVo);
        return new BaseResponse();
    }

    public BaseResponse updateState(TaskBoardUpdateStateRequest taskBoardUpdateStateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasTaskBoardAccess(taskBoardUpdateStateRequest, currentAccountId);
        log.info("Update task board state has started. currentAccountId: {}", currentAccountId);
        UpdateTaskBoardStateVo updateTaskBoardStateVo = taskBoardUpdateRequestConverter.convert(taskBoardUpdateStateRequest);
        taskBoardOperationService.updateState(updateTaskBoardStateVo);
        return new BaseResponse();
    }

    private void validateHasTaskBoardAccess(TaskBoardUpdateRequest request, String currentAccountId) {
        taskBoardAccessValidator.validateHasTaskBoardAccess(request.getTaskBoardId(), currentAccountId);
    }

    private TaskBoardResponse mapResponse(TaskBoardDto taskBoardDto) {
        TaskBoardResponse taskBoardResponse = new TaskBoardResponse();
        taskBoardResponse.setTaskBoardDto(taskBoardDto);
        return taskBoardResponse;
    }
}
