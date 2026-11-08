package co.jinear.core.service.note;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.repository.cache.notefts.NoteFtsDirtyStateRepository;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteFtsRefreshService {

    private static final String REFRESH_FTS_MV_SQL = "refresh materialized view concurrently mv_note_fts";
    private static final String REFRESH_LOCK_KEY = "mv-note-fts";

    private final NoteFtsDirtyStateRepository noteFtsDirtyStateRepository;
    private final LockService lockService;
    private final JdbcTemplate jdbcTemplate;

    public void markDirty() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            noteFtsDirtyStateRepository.markDirty();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                noteFtsDirtyStateRepository.markDirty();
            }
        });
    }

    public void refreshIfDirty() {
        if (!noteFtsDirtyStateRepository.isDirty()) {
            return;
        }
        if (!tryAcquireRefreshLock()) {
            log.info("Refresh note fts mv skipped, a refresh is already in progress.");
            return;
        }
        try {
            noteFtsDirtyStateRepository.clearDirty();
            refresh();
        } catch (Exception e) {
            noteFtsDirtyStateRepository.markDirty();
            log.error("Refresh note fts mv has failed. Will retry on next tick.", e);
        } finally {
            releaseRefreshLock();
        }
    }

    private void refresh() {
        long start = System.currentTimeMillis();
        log.info("Refresh note fts mv has started.");
        jdbcTemplate.execute(REFRESH_FTS_MV_SQL);
        long duration = (System.currentTimeMillis() - start);
        log.info("Refresh note fts mv has completed. duration: {}", duration);
    }

    private boolean tryAcquireRefreshLock() {
        try {
            lockService.lock(REFRESH_LOCK_KEY, LockSourceType.NOTE_FTS_REFRESH);
            return true;
        } catch (LockedException e) {
            return false;
        }
    }

    private void releaseRefreshLock() {
        lockService.unlock(REFRESH_LOCK_KEY, LockSourceType.NOTE_FTS_REFRESH);
    }
}
