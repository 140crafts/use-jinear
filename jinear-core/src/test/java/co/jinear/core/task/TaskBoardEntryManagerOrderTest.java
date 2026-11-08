package co.jinear.core.task;

import co.jinear.core.converter.task.TaskBoardEntryFilterRequestToTaskSearchFilterVoConverter;
import co.jinear.core.converter.task.TaskBoardEntryInitializeRequestConverter;
import co.jinear.core.converter.task.TaskDtoToTaskBoardEntryDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.model.dto.task.PlainTaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.board.TaskBoardRetrieveService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryListingService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryOperationService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.task.TaskBoardAccessValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskBoardEntryManagerOrderTest {

    private final TaskBoardEntryOperationService taskBoardEntryOperationService = Mockito.mock(TaskBoardEntryOperationService.class);
    private final TaskBoardEntryRetrieveService taskBoardEntryRetrieveService = Mockito.mock(TaskBoardEntryRetrieveService.class);
    private final TaskBoardEntryManager taskBoardEntryManager = new TaskBoardEntryManager(
            Mockito.mock(TaskBoardAccessValidator.class),
            Mockito.mock(SessionInfoService.class),
            taskBoardEntryOperationService,
            taskBoardEntryRetrieveService,
            Mockito.mock(TaskBoardEntryListingService.class),
            Mockito.mock(TaskBoardEntryInitializeRequestConverter.class),
            Mockito.mock(PassiveService.class),
            Mockito.mock(TaskBoardRetrieveService.class),
            Mockito.mock(TaskActivityService.class),
            Mockito.mock(TaskRetrieveService.class),
            Mockito.mock(TaskBoardEntryFilterRequestToTaskSearchFilterVoConverter.class),
            Mockito.mock(TeamMemberRetrieveService.class),
            Mockito.mock(TaskListingService.class),
            Mockito.mock(TaskDtoToTaskBoardEntryDtoConverter.class));

    @BeforeEach
    void anOpenBoardWithOrdersZeroToTwo() {
        TaskBoardEntryDto entry = new TaskBoardEntryDto();
        entry.setTaskBoardId("board-1");
        TaskBoardDto board = new TaskBoardDto();
        board.setState(TaskBoardStateType.OPEN);
        PlainTaskBoardEntryDto plainEntry = new PlainTaskBoardEntryDto();
        plainEntry.setTaskBoard(board);
        Mockito.when(taskBoardEntryRetrieveService.retrieve("entry-1")).thenReturn(entry);
        Mockito.when(taskBoardEntryRetrieveService.retrievePlain("entry-1")).thenReturn(plainEntry);
        Mockito.when(taskBoardEntryRetrieveService.retrieveLastOrder("board-1")).thenReturn(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3, 99})
    void anOrderOutsideTheBoardIsRefused(int newOrder) {
        assertThatThrownBy(() -> taskBoardEntryManager.changeOrder("entry-1", newOrder))
                .isInstanceOf(BusinessException.class)
                .hasMessage("task-board.entry.order-out-of-range");
        Mockito.verifyNoInteractions(taskBoardEntryOperationService);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void anOrderInsideTheBoardIsApplied(int newOrder) {
        taskBoardEntryManager.changeOrder("entry-1", newOrder);

        Mockito.verify(taskBoardEntryOperationService).changeOrder("entry-1", newOrder);
    }
}
