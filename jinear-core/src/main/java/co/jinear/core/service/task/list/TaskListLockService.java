package co.jinear.core.service.task.list;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListLockService {

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 8, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lockTaskListForUpdate(String taskListId) {
        log.info("Lock task list for update has started. taskListId: {}", taskListId);
        lockService.lock(taskListId, LockSourceType.TASK_LIST_EDIT);
        log.info("Lock task list for update has completed. taskListId: {}", taskListId);
    }

    public void unlockTaskListForUpdate(String taskListId) {
        log.info("Unlock task list for update has started. taskListId: {}", taskListId);
        lockService.unlock(taskListId, LockSourceType.TASK_LIST_EDIT);
        log.info("Unlock task list for update has completed. taskListId: {}", taskListId);
    }
}
