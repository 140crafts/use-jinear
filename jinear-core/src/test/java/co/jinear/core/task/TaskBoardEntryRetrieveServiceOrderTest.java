package co.jinear.core.task;

import co.jinear.core.converter.task.PlainTaskBoardEntryDtoConverter;
import co.jinear.core.converter.task.TaskBoardEntryDtoConverter;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import co.jinear.core.repository.TaskBoardEntryRepository;
import co.jinear.core.service.task.board.entry.TaskBoardEntryRetrieveService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskBoardEntryRetrieveServiceOrderTest {

    private final TaskBoardEntryRepository taskBoardEntryRepository = Mockito.mock(TaskBoardEntryRepository.class);
    private final TaskBoardEntryRetrieveService taskBoardEntryRetrieveService = new TaskBoardEntryRetrieveService(
            taskBoardEntryRepository,
            Mockito.mock(TaskBoardEntryDtoConverter.class),
            Mockito.mock(PlainTaskBoardEntryDtoConverter.class));

    @Test
    void theNextOrderOnAnEmptyBoardIsZero() {
        Mockito.when(taskBoardEntryRepository.findFirstByTaskBoardIdAndPassiveIdIsNullOrderByOrderDesc("board-1")).thenReturn(Optional.empty());

        assertThat(taskBoardEntryRetrieveService.retrieveNextOrder("board-1")).isZero();
    }

    @Test
    void theNextOrderFollowsTheLastOrder() {
        TaskBoardEntry last = new TaskBoardEntry();
        last.setOrder(3);
        Mockito.when(taskBoardEntryRepository.findFirstByTaskBoardIdAndPassiveIdIsNullOrderByOrderDesc("board-1")).thenReturn(Optional.of(last));

        assertThat(taskBoardEntryRetrieveService.retrieveNextOrder("board-1")).isEqualTo(4);
    }
}
