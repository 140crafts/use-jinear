package co.jinear.core.task;

import co.jinear.core.converter.task.TaskBoardInitializeRequestConverter;
import co.jinear.core.converter.task.TaskBoardUpdateRequestConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.model.request.task.TaskBoardInitializeRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateColorRequest;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.board.TaskBoardOperationService;
import co.jinear.core.validator.common.ColorValidator;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskBoardManagerColorTest {

    private final TaskBoardOperationService taskBoardOperationService = Mockito.mock(TaskBoardOperationService.class);
    private final TaskBoardManager taskBoardManager = new TaskBoardManager(
            taskBoardOperationService,
            Mockito.mock(TaskBoardAccessValidator.class),
            Mockito.mock(SessionInfoService.class),
            Mockito.mock(TeamAccessValidator.class),
            new ColorValidator(),
            Mockito.mock(TaskBoardInitializeRequestConverter.class),
            Mockito.mock(TaskBoardUpdateRequestConverter.class));

    @Test
    void updateColorRefusesAColorThatIsNotHex() {
        TaskBoardUpdateColorRequest request = new TaskBoardUpdateColorRequest();
        request.setTaskBoardId("board-1");
        request.setColor("GGGGGG");

        assertThatThrownBy(() -> taskBoardManager.updateColor(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("common.error.invalid-color");
        Mockito.verifyNoInteractions(taskBoardOperationService);
    }

    @Test
    void updateColorAcceptsSixHexDigits() {
        TaskBoardUpdateColorRequest request = new TaskBoardUpdateColorRequest();
        request.setTaskBoardId("board-1");
        request.setColor("FF6600");

        taskBoardManager.updateColor(request);

        Mockito.verify(taskBoardOperationService).updateColor(Mockito.any());
    }

    @Test
    void initializeRefusesAColorThatIsNotHex() {
        TaskBoardInitializeRequest request = new TaskBoardInitializeRequest();
        request.setTeamId("team-1");
        request.setColor("ZZZZZZ");

        assertThatThrownBy(() -> taskBoardManager.initializeTaskBoard(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("common.error.invalid-color");
        Mockito.verifyNoInteractions(taskBoardOperationService);
    }
}
