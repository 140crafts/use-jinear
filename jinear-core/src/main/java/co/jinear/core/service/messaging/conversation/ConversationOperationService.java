package co.jinear.core.service.messaging.conversation;

import co.jinear.core.model.dto.messaging.conversation.ConversationDto;
import co.jinear.core.model.entity.messaging.Conversation;
import co.jinear.core.model.vo.messaging.conversation.InitializeConversationVo;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.repository.messaging.ConversationRepository;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantKeyCalculateService;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantOperationService;
import co.jinear.core.service.messaging.message.MessageOperationService;
import co.jinear.core.system.NormalizeHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationOperationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantOperationService conversationParticipantOperationService;
    private final ConversationLockService conversationLockService;
    private final MessageOperationService messageOperationService;
    private final ConversationParticipantKeyCalculateService conversationParticipantKeyCalculateService;
    private final ConversationRetrieveService conversationRetrieveService;

    @Transactional
    public String initializeAndRetrieveConversationId(InitializeConversationVo initializeConversationVo) {
        Set<String> participantAccountIds = initializeConversationVo.getParticipantAccountIds();
        String participantsKey = conversationParticipantKeyCalculateService.calculate(participantAccountIds);
        conversationLockService.lockConversationInit(participantsKey);
        try {
            log.info("Initialize conversation has started. participantAccountIds: {}", NormalizeHelper.listToString(participantAccountIds));
            String conversationId = retrieveConversationId(initializeConversationVo, participantsKey);
            initializeFirstMessage(initializeConversationVo, conversationId);
            return conversationId;
        } finally {
            conversationLockService.unlockConversationInit(participantsKey);
        }
    }

    private String retrieveConversationId(InitializeConversationVo initializeConversationVo, String participantsKey) {
        log.info("Retrieve conversation id has started. participantsKey: {}", participantsKey);
        Optional<ConversationDto> optionalConversationDto = conversationRetrieveService.retrieveWithParticipantsKey(participantsKey);
        return optionalConversationDto
                .map(ConversationDto::getConversationId)
                .orElseGet(() -> initializeAndAssignParticipants(initializeConversationVo, participantsKey));
    }

    private String initializeAndAssignParticipants(InitializeConversationVo initializeConversationVo, String participantsKey) {
        String conversationId = initializeConversation(initializeConversationVo, participantsKey);
        Set<String> participantAccountIds = initializeConversationVo.getParticipantAccountIds();
        participantAccountIds.forEach(accId -> conversationParticipantOperationService.initializeParticipant(conversationId, accId));
        return conversationId;
    }

    private String initializeConversation(InitializeConversationVo initializeConversationVo, String participantsKey) {
        Conversation conversation = mapConversation(initializeConversationVo.getWorkspaceId(), participantsKey);
        Conversation saved = conversationRepository.save(conversation);
        return saved.getConversationId();
    }

    private Conversation mapConversation(String workspaceId, String participantsKey) {
        Conversation conversation = new Conversation();
        conversation.setWorkspaceId(workspaceId);
        conversation.setLastActivityTime(ZonedDateTime.now());
        conversation.setParticipantsKey(participantsKey);
        return conversation;
    }

    private void initializeFirstMessage(InitializeConversationVo initializeConversationVo, String conversationId) {
        InitializeMessageVo initializeMessageVo = new InitializeMessageVo();
        initializeMessageVo.setConversationId(conversationId);
        initializeMessageVo.setAccountId(initializeConversationVo.getInitializedBy());
        initializeMessageVo.setBody(initializeConversationVo.getInitialMessageBody());
        messageOperationService.initialize(initializeMessageVo);
    }
}
