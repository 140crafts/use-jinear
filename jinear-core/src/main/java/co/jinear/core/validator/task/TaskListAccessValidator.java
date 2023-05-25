package co.jinear.core.validator.task;

import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.service.task.list.TaskListRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskListAccessValidator {

    private final TaskListRetrieveService taskListRetrieveService;
    private final TeamAccessValidator teamAccessValidator;

    public void validateHasTaskListAccess(String taskListId, String currentAccountId) {
        TaskListDto taskListDto = taskListRetrieveService.retrieve(taskListId);
        teamAccessValidator.validateTeamAccess(currentAccountId, taskListDto.getTeamId());
    }
}
