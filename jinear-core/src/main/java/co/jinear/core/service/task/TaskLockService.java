package co.jinear.core.service.task;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import co.jinear.core.service.topic.TopicSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskLockService {

    private final LockService lockService;

    @Retryable(includes = LockedException.class, maxRetries = 7, delay = 200, multiplier = 2, maxDelay = 3000)
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
