package co.jinear.core.service.task.board;

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
public class TaskBoardLockService {

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 8, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lockTaskBoardForUpdate(String taskBoardId) {
        log.info("Lock task list for update has started. taskBoardId: {}", taskBoardId);
        lockService.lock(taskBoardId, LockSourceType.TASK_BOARD_EDIT);
        log.info("Lock task list for update has completed. taskBoardId: {}", taskBoardId);
    }

    public void unlockTaskBoardForUpdate(String taskBoardId) {
        log.info("Unlock task list for update has started. taskBoardId: {}", taskBoardId);
        lockService.unlock(taskBoardId, LockSourceType.TASK_BOARD_EDIT);
        log.info("Unlock task list for update has completed. taskBoardId: {}", taskBoardId);
    }
}
