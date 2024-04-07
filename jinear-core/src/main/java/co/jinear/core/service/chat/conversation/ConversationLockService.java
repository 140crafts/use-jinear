package co.jinear.core.service.chat.conversation;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationLockService {

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 4, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lockConversationInit(List<String> participantAccountIds) {
        int participantAccountIdsHash = participantAccountIds.hashCode();
        log.info("Lock conversation init has started. participantAccountIdsHash: {}", participantAccountIdsHash);
        lockService.lock(String.valueOf(participantAccountIdsHash), LockSourceType.CONVERSATION_INIT);
        log.info("Lock conversation init has completed. participantAccountIdsHash: {}", participantAccountIdsHash);
    }

    public void unlockConversationInit(List<String> participantAccountIds) {
        int participantAccountIdsHash = participantAccountIds.hashCode();
        log.info("Unlock conversation init has started. participantAccountIdsHash: {}", participantAccountIdsHash);
        lockService.unlock(String.valueOf(participantAccountIdsHash), LockSourceType.CONVERSATION_INIT);
        log.info("Unlock conversation init has completed. participantAccountIdsHash: {}", participantAccountIdsHash);
    }
}
