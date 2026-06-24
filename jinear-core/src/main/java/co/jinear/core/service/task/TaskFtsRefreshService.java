package co.jinear.core.service.task;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.repository.cache.taskfts.TaskFtsDirtyStateRepository;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFtsRefreshService {

    private static final String REFRESH_FTS_MV_SQL = "refresh materialized view concurrently mv_task_fts";
    private static final String REFRESH_LOCK_KEY = "mv-task-fts";

    private final TaskFtsDirtyStateRepository taskFtsDirtyStateRepository;
    private final LockService lockService;
    private final JdbcTemplate jdbcTemplate;

    public void markDirty() {
        taskFtsDirtyStateRepository.markDirty();
    }

    public void refreshIfDirty() {
        if (!taskFtsDirtyStateRepository.isDirty()) {
            return;
        }
        if (!tryAcquireRefreshLock()) {
            log.info("Refresh task fts mv skipped, a refresh is already in progress.");
            return;
        }
        try {
            taskFtsDirtyStateRepository.clearDirty();
            refresh();
        } catch (Exception e) {
            taskFtsDirtyStateRepository.markDirty();
            log.error("Refresh task fts mv has failed. Will retry on next tick.", e);
        } finally {
            releaseRefreshLock();
        }
    }

    private void refresh() {
        long start = System.currentTimeMillis();
        log.info("Refresh task fts mv has started.");
        jdbcTemplate.execute(REFRESH_FTS_MV_SQL);
        long duration = (System.currentTimeMillis() - start);
        log.info("Refresh task fts mv has completed. duration: {}", duration);
    }

    private boolean tryAcquireRefreshLock() {
        try {
            lockService.lock(REFRESH_LOCK_KEY, LockSourceType.TASK_FTS_REFRESH);
            return true;
        } catch (LockedException e) {
            return false;
        }
    }

    private void releaseRefreshLock() {
        lockService.unlock(REFRESH_LOCK_KEY, LockSourceType.TASK_FTS_REFRESH);
    }
}
