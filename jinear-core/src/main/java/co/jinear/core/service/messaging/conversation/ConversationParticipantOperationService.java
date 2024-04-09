package co.jinear.core.service.messaging.conversation;

import co.jinear.core.model.entity.messaging.ConversationParticipant;
import co.jinear.core.repository.messaging.ConversationParticipantRepository;
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

    public void addParticipant(String conversationId, String accountId) {
        log.info("Add participant to conversation has started. conversationId: {}, accountId: {}", conversationId, accountId);
        conversationParticipantRetrieveService.validateParticipantIsNotAlreadyInConversation(conversationId, accountId);
        ConversationParticipant conversationParticipant = new ConversationParticipant();
        conversationParticipant.setConversationId(conversationId);
        conversationParticipant.setAccountId(accountId);
        conversationParticipantRepository.save(conversationParticipant);
    }

    public void removeParticipant(String conversationParticipantId, String passiveId) {
        log.info("Remove participant has started. conversationParticipantId: {}", conversationParticipantId);
        ConversationParticipant conversationParticipant = conversationParticipantRetrieveService.retrieveEntity(conversationParticipantId);
        conversationParticipant.setPassiveId(passiveId);
        conversationParticipant.setLeftAt(ZonedDateTime.now());
        conversationParticipantRepository.save(conversationParticipant);
    }
}
