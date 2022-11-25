package co.jinear.core.service.task;

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
public class TaskLockService {

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 8, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lockTopicForTaskInitialization(String topicId) {
        log.info("Lock topic for task initialization has started for topicId: {}", topicId);
        lockService.lock(topicId, LockSourceType.TOPIC_TASK_INIT);
        log.info("Lock topic for task initialization has ended for topicId: {}", topicId);
    }

    public void unlockTopicForTaskInitialization(String topicId) {
        log.info("Unlock topic for task initialization has started for topicId: {}", topicId);
        lockService.unlock(topicId, LockSourceType.TOPIC_TASK_INIT);
        log.info("Unlock topic for task initialization has ended for topicId: {}", topicId);
    }
}
