package co.jinear.core.service.messaging.conversation.participant;

import co.jinear.core.converter.messaging.conversation.ConversationParticipantDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.entity.messaging.ConversationParticipant;
import co.jinear.core.repository.messaging.ConversationParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationParticipantRetrieveService {

    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationParticipantDtoConverter conversationParticipantDtoConverter;

    public ConversationParticipant retrieveEntity(String conversationParticipantId) {
        return conversationParticipantRepository.findByConversationParticipantIdAndPassiveIdIsNull(conversationParticipantId)
                .orElseThrow(NotFoundException::new);
    }

    public ConversationParticipantDto retrieve(String accountId, String conversationId) {
        return conversationParticipantRepository.findByConversationIdAndAccountIdAndLeftAtIsNullAndPassiveIdIsNull(conversationId, accountId)
                .map(conversationParticipantDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public void validateParticipantIsNotAlreadyInConversation(String conversationId, String accountId) {
        log.info("Check participant is already in conversation has started. conversationId: {}, accountId: {}", conversationId, accountId);
        if (checkParticipantIsInConversation(conversationId, accountId)) {
            throw new BusinessException("messaging.conversation.already-participant");
        }
    }

    public void validateParticipantIsInConversation(String accountId, String conversationId) {
        long start = System.currentTimeMillis();
        log.info("Validate participant is in conversation has started. accountId: {}, conversationId: {}", accountId, conversationId);
        if (Boolean.FALSE.equals(checkParticipantIsInConversation(conversationId, accountId))) {
            throw new NoAccessException();
        }
        log.info("Validate participant is in conversation has completed. Duration: {}ms", System.currentTimeMillis() - start);
    }

    private boolean checkParticipantIsInConversation(String conversationId, String accountId) {
        return conversationParticipantRepository.existsByConversationIdAndAccountIdAndLeftAtIsNullAndPassiveIdIsNull(conversationId, accountId);
    }
}
