package co.jinear.core.validator.task;


import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskAccessValidator {

    private final TaskRetrieveService taskRetrieveService;
    private final TeamAccessValidator teamAccessValidator;
    private final WorkspaceValidator workspaceValidator;

    public void validateTaskAccess(String accountId, String taskId) {
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        final String workspaceId = taskDto.getWorkspaceId();
        final String teamId = taskDto.getTeamId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        teamAccessValidator.validateTeamAccess(accountId, workspaceId, teamId);
    }
}
