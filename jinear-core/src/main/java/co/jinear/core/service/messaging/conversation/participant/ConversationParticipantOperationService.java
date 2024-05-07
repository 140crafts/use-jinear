package co.jinear.core.service.messaging.conversation.participant;

import co.jinear.core.model.entity.messaging.ConversationParticipant;
import co.jinear.core.repository.messaging.ConversationParticipantRepository;
import co.jinear.core.service.messaging.conversation.ConversationLockService;
import co.jinear.core.service.messaging.conversation.ConversationUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationParticipantOperationService {

    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationParticipantRetrieveService conversationParticipantRetrieveService;
    private final ConversationUpdateService conversationUpdateService;
    private final ConversationLockService conversationLockService;

    public void initializeParticipant(String conversationId, String accountId) {
        log.info("Initialize participant has started. conversationId: {}, accountId: {}", conversationId, accountId);
        ConversationParticipant conversationParticipant = new ConversationParticipant();
        conversationParticipant.setConversationId(conversationId);
        conversationParticipant.setAccountId(accountId);
        conversationParticipantRepository.save(conversationParticipant);
    }

    public void addParticipant(String conversationId, String accountId) {
        log.info("Add participant to conversation has started. conversationId: {}, accountId: {}", conversationId, accountId);
        conversationLockService.lockConversation(conversationId);
        try {
            conversationParticipantRetrieveService.validateParticipantIsNotAlreadyInConversation(conversationId, accountId);
            initializeParticipant(conversationId, accountId);
            conversationUpdateService.recalculateAndUpdateParticipantKey(conversationId);
        } finally {
            conversationLockService.unlockConversation(conversationId);
        }
    }

    public void removeParticipant(String conversationParticipantId, String passiveId) {
        log.info("Remove participant has started. conversationParticipantId: {}", conversationParticipantId);
        ConversationParticipant conversationParticipant = conversationParticipantRetrieveService.retrieveEntity(conversationParticipantId);
        String conversationId = conversationParticipant.getConversationId();
        conversationLockService.lockConversation(conversationId);
        try {
            conversationParticipant.setPassiveId(passiveId);
            conversationParticipant.setLeftAt(ZonedDateTime.now());
            conversationParticipantRepository.save(conversationParticipant);
            conversationUpdateService.recalculateAndUpdateParticipantKey(conversationId);
        } finally {
            conversationLockService.unlockConversation(conversationId);
        }
    }

    public void updateSilentUntil(String conversationParticipantId, ZonedDateTime silentUntil) {
        log.info("Update silent until has started. conversationParticipantId: {}, silentUntil: {}", conversationParticipantId, silentUntil);
        ConversationParticipant conversationParticipant = conversationParticipantRetrieveService.retrieveEntity(conversationParticipantId);
        conversationParticipant.setSilentUntil(silentUntil);
        conversationParticipantRepository.save(conversationParticipant);
    }

    @Transactional
    public void updateLastCheck(String conversationId, String accountId, ZonedDateTime lastCheck) {
        log.info("Update last check has started. conversationId: {}, accountId: {}, lastCheck: {}", conversationId, accountId, lastCheck);
        conversationParticipantRepository.updateLastCheck(conversationId, accountId, lastCheck);
    }
}
