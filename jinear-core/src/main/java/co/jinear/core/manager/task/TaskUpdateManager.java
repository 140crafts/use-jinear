package co.jinear.core.manager.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskUpdateRequest;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskUpdateService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateManager {

    private final ModelMapper modelMapper;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskUpdateService taskUpdateService;

    public TaskResponse updateTask(TaskUpdateRequest taskUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateHasWorkspaceAccess(taskUpdateRequest, currentAccountId);
        log.info("Update task has started. accountId: {}, taskUpdateRequest: {}", currentAccountId, taskUpdateRequest);
        TaskUpdateVo taskUpdateVo = modelMapper.map(taskUpdateRequest, TaskUpdateVo.class);
        TaskDto taskDto = taskUpdateService.updateTask(taskUpdateVo);
        return mapResponse(taskDto);
    }

    private void validateHasWorkspaceAccess(TaskUpdateRequest taskUpdateRequest, String currentAccountId) {
        TaskDto taskDto = taskRetrieveService.retrieve(taskUpdateRequest.getTaskId());
        workspaceValidator.validateHasAccess(currentAccountId, taskDto.getWorkspaceId());
    }

    private TaskResponse mapResponse(TaskDto taskDto) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskDto(taskDto);
        return taskResponse;
    }
}
