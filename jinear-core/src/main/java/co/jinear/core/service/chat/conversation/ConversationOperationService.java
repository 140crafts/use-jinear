package co.jinear.core.service.chat.conversation;

import co.jinear.core.model.entity.chat.Conversation;
import co.jinear.core.model.vo.chat.conversation.InitializeConversationVo;
import co.jinear.core.repository.chat.ConversationRepository;
import co.jinear.core.system.NormalizeHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationOperationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRetrieveService conversationParticipantRetrieveService;
    private final ConversationParticipantOperationService conversationParticipantOperationService;
    private final ConversationLockService conversationLockService;

    @Transactional
    public void initialize(InitializeConversationVo initializeConversationVo) {
        List<String> participantAccountIds = initializeConversationVo.getParticipantAccountIds();
        conversationLockService.lockConversationInit(participantAccountIds);
        try {
            log.info("Initialize conversation has started. participantAccountIds: {}", NormalizeHelper.listToString(participantAccountIds));
            conversationParticipantRetrieveService.validateConversationNotExistsBetweenAccounts(participantAccountIds);
            Conversation conversation = initialize();
            participantAccountIds.forEach(accId -> conversationParticipantOperationService.addParticipant(conversation.getConversationId(), accId));
        } finally {
            conversationLockService.unlockConversationInit(participantAccountIds);
        }
    }

    private Conversation initialize() {
        Conversation conversation = new Conversation();
        return conversationRepository.save(conversation);
    }
}
