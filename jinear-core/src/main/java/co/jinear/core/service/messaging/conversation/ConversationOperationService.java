package co.jinear.core.service.messaging.conversation;

import co.jinear.core.model.entity.messaging.Conversation;
import co.jinear.core.model.vo.messaging.conversation.InitializeConversationVo;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.repository.messaging.ConversationRepository;
import co.jinear.core.service.messaging.message.MessageOperationService;
import co.jinear.core.system.NormalizeHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationOperationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRetrieveService conversationParticipantRetrieveService;
    private final ConversationParticipantOperationService conversationParticipantOperationService;
    private final ConversationLockService conversationLockService;
    private final MessageOperationService messageOperationService;

    @Transactional
    public void initialize(InitializeConversationVo initializeConversationVo) {
        List<String> participantAccountIds = initializeConversationVo.getParticipantAccountIds();
        conversationLockService.lockConversationInit(participantAccountIds);
        try {
            log.info("Initialize conversation has started. participantAccountIds: {}", NormalizeHelper.listToString(participantAccountIds));
            conversationParticipantRetrieveService.validateConversationNotExistsBetweenAccounts(participantAccountIds);
            Conversation conversation = initialize();
            participantAccountIds.forEach(accId -> conversationParticipantOperationService.addParticipant(conversation.getConversationId(), accId));
            initializeFirstMessage(initializeConversationVo, conversation);
        } finally {
            conversationLockService.unlockConversationInit(participantAccountIds);
        }
    }

    private void initializeFirstMessage(InitializeConversationVo initializeConversationVo, Conversation conversation) {
        InitializeMessageVo initializeMessageVo = new InitializeMessageVo();
        initializeMessageVo.setConversationId(conversation.getConversationId());
        initializeMessageVo.setAccountId(initializeConversationVo.getInitializedBy());
        initializeMessageVo.setBody(initializeConversationVo.getInitialMessageBody());
        messageOperationService.initialize(initializeMessageVo);
    }

    private Conversation initialize() {
        Conversation conversation = new Conversation();
        conversation.setLastActivityTime(ZonedDateTime.now());
        return conversationRepository.save(conversation);
    }
}
