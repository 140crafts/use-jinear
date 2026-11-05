package co.jinear.core.task;

import co.jinear.core.converter.task.InitializeTaskBoardEntryVoToEntityConverter;
import co.jinear.core.converter.task.TaskBoardEntryDtoConverter;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import co.jinear.core.model.vo.task.InitializeTaskBoardEntryVo;
import co.jinear.core.repository.TaskBoardEntryRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.board.TaskBoardLockService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryOperationService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryRetrieveService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskBoardEntryOperationServiceOrderTest {

    private final TaskBoardEntryRetrieveService taskBoardEntryRetrieveService = Mockito.mock(TaskBoardEntryRetrieveService.class);
    private final TaskBoardEntryRepository taskBoardEntryRepository = Mockito.mock(TaskBoardEntryRepository.class);
    private final PassiveService passiveService = Mockito.mock(PassiveService.class);
    private final TaskBoardLockService taskBoardLockService = Mockito.mock(TaskBoardLockService.class);
    private final InitializeTaskBoardEntryVoToEntityConverter initializeConverter = Mockito.mock(InitializeTaskBoardEntryVoToEntityConverter.class);
    private final TaskBoardEntryOperationService taskBoardEntryOperationService = new TaskBoardEntryOperationService(
            taskBoardEntryRetrieveService,
            taskBoardEntryRepository,
            initializeConverter,
            Mockito.mock(TaskBoardEntryDtoConverter.class),
            taskBoardLockService,
            passiveService);

    @Test
    void aMoveDownTakesTheOrderOfTheEntryAbove() {
        board("a", 0, "x", 1, "b", 2, "c", 3);

        taskBoardEntryOperationService.changeFilteredOrder("x", "b", "c");

        Mockito.verify(taskBoardEntryRepository).updateOrderDownward("board-1", 1, 2);
        assertThat(savedOrder()).isEqualTo(2);
    }

    @Test
    void aMoveUpTakesTheOrderOfTheEntryBelow() {
        board("a", 0, "b", 1, "c", 2, "x", 3);

        taskBoardEntryOperationService.changeFilteredOrder("x", "a", "b");

        Mockito.verify(taskBoardEntryRepository).updateOrderUpward("board-1", 3, 1);
        assertThat(savedOrder()).isEqualTo(1);
    }

    @Test
    void aMoveToTheTopTakesOrderZero() {
        board("a", 0, "b", 1, "x", 2);

        taskBoardEntryOperationService.changeFilteredOrder("x", "", "a");

        Mockito.verify(taskBoardEntryRepository).updateOrderUpward("board-1", 2, 0);
        assertThat(savedOrder()).isZero();
    }

    @Test
    void aMoveToTheBottomTakesTheLastOrder() {
        board("a", 0, "x", 1, "b", 2, "c", 3);

        taskBoardEntryOperationService.changeFilteredOrder("x", "c", null);

        Mockito.verify(taskBoardEntryRepository).updateOrderDownward("board-1", 1, 3);
        assertThat(savedOrder()).isEqualTo(3);
    }

    @Test
    void aDeletedEntryMovesToTheLastPlaceBeforeItTurnsPassive() {
        board("a", 0, "x", 1, "b", 2, "c", 3);
        Mockito.when(taskBoardEntryRetrieveService.retrieveLastOrder("board-1")).thenReturn(3);
        Mockito.when(passiveService.createUserActionPassive()).thenReturn("passive-1");

        taskBoardEntryOperationService.deleteEntry("x");

        Mockito.verify(taskBoardEntryRepository).updateOrderDownward("board-1", 1, 3);
        ArgumentCaptor<TaskBoardEntry> captor = ArgumentCaptor.forClass(TaskBoardEntry.class);
        Mockito.verify(taskBoardEntryRepository, Mockito.times(2)).save(captor.capture());
        TaskBoardEntry passive = captor.getAllValues().get(1);
        assertThat(passive.getOrder()).isEqualTo(3);
        assertThat(passive.getPassiveId()).isEqualTo("passive-1");
    }

    @Test
    void aMoveLocksTheBoardOnceAndUnlocksItAfterwards() {
        board("a", 0, "x", 1, "b", 2, "c", 3);

        taskBoardEntryOperationService.changeFilteredOrder("x", "b", "c");

        InOrder inOrder = Mockito.inOrder(taskBoardLockService, taskBoardEntryRepository);
        inOrder.verify(taskBoardLockService).lockTaskBoardForUpdate("board-1");
        inOrder.verify(taskBoardEntryRepository).updateOrderDownward("board-1", 1, 2);
        inOrder.verify(taskBoardLockService).unlockTaskBoardForUpdate("board-1");
        Mockito.verify(taskBoardLockService, Mockito.times(1)).lockTaskBoardForUpdate("board-1");
    }

    @Test
    void aDeleteLocksTheBoardOnceAndUnlocksItAfterwards() {
        board("a", 0, "x", 1, "b", 2);
        Mockito.when(taskBoardEntryRetrieveService.retrieveLastOrder("board-1")).thenReturn(2);

        taskBoardEntryOperationService.deleteEntry("x");

        Mockito.verify(taskBoardLockService, Mockito.times(1)).lockTaskBoardForUpdate("board-1");
        Mockito.verify(taskBoardLockService).unlockTaskBoardForUpdate("board-1");
    }

    @Test
    void theBoardIsUnlockedWhenTheMoveFails() {
        board("a", 0, "x", 1);
        Mockito.doThrow(new IllegalStateException()).when(taskBoardEntryRepository).updateOrderUpward("board-1", 1, 0);

        assertThatThrownBy(() -> taskBoardEntryOperationService.changeOrder("x", 0)).isInstanceOf(IllegalStateException.class);

        Mockito.verify(taskBoardLockService).unlockTaskBoardForUpdate("board-1");
    }

    @Test
    void aNewEntryGoesAfterTheLastOrderEvenWhenTheBoardHasAGap() {
        InitializeTaskBoardEntryVo vo = new InitializeTaskBoardEntryVo();
        vo.setTaskBoardId("board-1");
        vo.setTaskId("task-1");
        Mockito.when(taskBoardEntryRetrieveService.retrieveNextOrder("board-1")).thenReturn(4);

        taskBoardEntryOperationService.initialize(vo);

        Mockito.verify(initializeConverter).map(vo, 4);
    }

    private void board(Object... idsAndOrders) {
        for (int index = 0; index < idsAndOrders.length; index += 2) {
            String taskBoardEntryId = (String) idsAndOrders[index];
            TaskBoardEntry entry = new TaskBoardEntry();
            entry.setTaskBoardEntryId(taskBoardEntryId);
            entry.setTaskBoardId("board-1");
            entry.setOrder((Integer) idsAndOrders[index + 1]);
            Mockito.when(taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId)).thenReturn(entry);
        }
        Mockito.when(taskBoardEntryRepository.save(Mockito.any(TaskBoardEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private Integer savedOrder() {
        ArgumentCaptor<TaskBoardEntry> captor = ArgumentCaptor.forClass(TaskBoardEntry.class);
        Mockito.verify(taskBoardEntryRepository).save(captor.capture());
        return captor.getValue().getOrder();
    }
}
