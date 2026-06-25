package co.jinear.core.service.richtext.sync;

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
public class RichTextSyncLockService {

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 8, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lock(String richTextId) {
        log.info("Lock rich text for sync has started for richTextId: {}", richTextId);
        lockService.lock(richTextId, LockSourceType.RICH_TEXT_SYNC);
        log.info("Lock rich text for sync has completed for richTextId: {}", richTextId);
    }

    public void unlock(String richTextId) {
        log.info("Unlock rich text for sync has started for richTextId: {}", richTextId);
        lockService.unlock(richTextId, LockSourceType.RICH_TEXT_SYNC);
        log.info("Unlock rich text for sync has completed for richTextId: {}", richTextId);
    }
}