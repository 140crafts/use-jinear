package co.jinear.core.task;

import co.jinear.core.repository.cache.taskfts.TaskFtsDirtyStateRepository;
import co.jinear.core.service.lock.LockService;
import co.jinear.core.service.task.TaskFtsRefreshService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class TaskFtsRefreshServiceTest {

    private final TaskFtsDirtyStateRepository dirtyState = Mockito.mock(TaskFtsDirtyStateRepository.class);
    private final TaskFtsRefreshService service = new TaskFtsRefreshService(
            dirtyState, Mockito.mock(LockService.class), Mockito.mock(JdbcTemplate.class));

    @AfterEach
    void clearSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void outsideATransactionTheFlagIsSetAtOnce() {
        service.markDirty();

        Mockito.verify(dirtyState).markDirty();
    }

    @Test
    void insideATransactionTheFlagWaitsForTheCommit() {
        TransactionSynchronizationManager.initSynchronization();

        service.markDirty();
        Mockito.verify(dirtyState, Mockito.never()).markDirty();

        TransactionSynchronizationManager.getSynchronizations().forEach(TransactionSynchronization::afterCommit);
        Mockito.verify(dirtyState).markDirty();
    }
}
