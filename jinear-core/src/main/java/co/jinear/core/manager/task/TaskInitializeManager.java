package co.jinear.core.manager.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskInitializeService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInitializeManager {

    private final TaskInitializeService taskInitializeService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final ModelMapper modelMapper;

    public TaskResponse initializeTask(TaskInitializeRequest taskInitializeRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, taskInitializeRequest);
        validateTeamAccess(currentAccount, taskInitializeRequest);
        log.info("Initialize task has started. currentAccount: {}", currentAccount);
        TaskInitializeVo taskInitializeVo = modelMapper.map(taskInitializeRequest, TaskInitializeVo.class);
        taskInitializeVo.setOwnerId(currentAccount);
        TaskDto taskDto = taskInitializeService.initializeTask(taskInitializeVo);
        return mapResponse(taskDto);
    }

    private void validateWorkspaceAccess(String accountId, TaskInitializeRequest taskInitializeRequest) {
        workspaceValidator.validateHasAccess(accountId, taskInitializeRequest.getWorkspaceId());
    }

    private void validateTeamAccess(String currentAccount, TaskInitializeRequest taskInitializeRequest) {
        teamAccessValidator.validateTeamAccess(currentAccount, taskInitializeRequest.getWorkspaceId(), taskInitializeRequest.getTeamId());
    }

    private TaskResponse mapResponse(TaskDto taskDto) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskDto(taskDto);
        return taskResponse;
    }
}
