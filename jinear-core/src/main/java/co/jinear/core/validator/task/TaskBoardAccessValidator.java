package co.jinear.core.validator.task;

import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.service.task.board.TaskBoardRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskBoardAccessValidator {

    private final TaskBoardRetrieveService taskBoardRetrieveService;
    private final TeamAccessValidator teamAccessValidator;

    public void validateHasTaskBoardAccess(String taskBoardId, String currentAccountId) {
        TaskBoardDto taskBoardDto = taskBoardRetrieveService.retrieve(taskBoardId);
        teamAccessValidator.validateTeamAccess(currentAccountId, taskBoardDto.getTeamId());
    }
}
