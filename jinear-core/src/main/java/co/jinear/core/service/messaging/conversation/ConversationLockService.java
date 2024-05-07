package co.jinear.core.service.messaging.conversation;

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
public class ConversationLockService {

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 4, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lockConversationInit(String participantsKey) {
        log.info("Lock conversation init has started. participantsKey: {}", participantsKey);
        lockService.lock(participantsKey, LockSourceType.CONVERSATION_INIT);
        log.info("Lock conversation init has completed. participantsKey: {}", participantsKey);
    }

    @Retryable(value = {LockedException.class}, maxAttempts = 4, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void lockConversation(String conversationId) {
        log.info("Lock conversation has started. conversationId: {}", conversationId);
        lockService.lock(conversationId, LockSourceType.CONVERSATION);
        log.info("Lock conversation has completed. conversationId: {}", conversationId);
    }

    public void unlockConversationInit(String participantsKey) {
        log.info("Unlock conversation init has started. participantsKey: {}", participantsKey);
        lockService.unlock(participantsKey, LockSourceType.CONVERSATION_INIT);
        log.info("Unlock conversation init has completed. participantsKey: {}", participantsKey);
    }

    public void unlockConversation(String conversationId) {
        log.info("Unlock conversation has started. conversationId: {}", conversationId);
        lockService.unlock(conversationId, LockSourceType.CONVERSATION);
        log.info("Unlock conversation has completed. conversationId: {}", conversationId);
    }
}
