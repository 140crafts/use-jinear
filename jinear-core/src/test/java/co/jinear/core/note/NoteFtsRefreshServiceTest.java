package co.jinear.core.note;

import co.jinear.core.repository.cache.notefts.NoteFtsDirtyStateRepository;
import co.jinear.core.service.lock.LockService;
import co.jinear.core.service.note.NoteFtsRefreshService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class NoteFtsRefreshServiceTest {

    private final NoteFtsDirtyStateRepository dirtyState = Mockito.mock(NoteFtsDirtyStateRepository.class);
    private final NoteFtsRefreshService service = new NoteFtsRefreshService(
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
