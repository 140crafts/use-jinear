package co.jinear.core.service.chat.conversation;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.chat.ConversationParticipant;
import co.jinear.core.repository.chat.ConversationParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationParticipantRetrieveService {

    private final ConversationParticipantRepository conversationParticipantRepository;

    public ConversationParticipant retrieveEntity(String conversationParticipantId) {
        return conversationParticipantRepository.findByConversationParticipantIdAndPassiveIdIsNull(conversationParticipantId)
                .orElseThrow(NotFoundException::new);
    }

    public void validateConversationNotExistsBetweenAccounts(List<String> participantAccountIds) {
        String existingConversationId = findConversationIdBetweenAccounts(participantAccountIds);
        if (Objects.nonNull(existingConversationId)) {
            throw new BusinessException();
        }
    }

    public String findConversationIdBetweenAccounts(List<String> accountIds) {
        log.info("Find conversation id between accounts has started. accountIds: {}", accountIds);
        return conversationParticipantRepository.findConversationIdBetweenParticipants(accountIds);
    }

    public void validateParticipantIsNotAlreadyInConversation(String conversationId, String accountId) {
        log.info("Check participant is already in conversation has started. conversationId: {}, accountId: {}", conversationId, accountId);
        if (checkParticipantIsAlreadyInConversation(conversationId, accountId)) {
            throw new BusinessException();
        }
    }

    private boolean checkParticipantIsAlreadyInConversation(String conversationId, String accountId) {
        return conversationParticipantRepository.existsByConversationIdAndAccountIdAndLeftAtIsNullAndPassiveIdIsNull(conversationId, accountId);
    }
}
