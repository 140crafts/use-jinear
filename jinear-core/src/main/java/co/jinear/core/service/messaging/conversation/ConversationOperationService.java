package co.jinear.core.service.messaging.conversation;

import co.jinear.core.exception.BusinessException;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public void initialize(InitializeConversationVo initializeConversationVo) {
        List<String> participantAccountIds = initializeConversationVo.getParticipantAccountIds();
        String participantsKey = conversationParticipantKeyCalculateService.calculate(participantAccountIds);
        conversationLockService.lockConversationInit(participantsKey);
        try {
            log.info("Initialize conversation has started. participantAccountIds: {}", NormalizeHelper.listToString(participantAccountIds));
            validateConversationNotExistsBetweenAccounts(participantsKey);
            Conversation conversation = initialize(initializeConversationVo.getWorkspaceId(), participantsKey);
            participantAccountIds.forEach(accId -> conversationParticipantOperationService.initializeParticipant(conversation.getConversationId(), accId));
            initializeFirstMessage(initializeConversationVo, conversation);
        } finally {
            conversationLockService.unlockConversationInit(participantsKey);
        }
    }

    private void initializeFirstMessage(InitializeConversationVo initializeConversationVo, Conversation conversation) {
        InitializeMessageVo initializeMessageVo = new InitializeMessageVo();
        initializeMessageVo.setConversationId(conversation.getConversationId());
        initializeMessageVo.setAccountId(initializeConversationVo.getInitializedBy());
        initializeMessageVo.setBody(initializeConversationVo.getInitialMessageBody());
        messageOperationService.initialize(initializeMessageVo);
    }

    private Conversation initialize(String workspaceId, String participantsKey) {
        Conversation conversation = new Conversation();
        conversation.setWorkspaceId(workspaceId);
        conversation.setLastActivityTime(ZonedDateTime.now());
        conversation.setParticipantsKey(participantsKey);
        return conversationRepository.save(conversation);
    }

    private void validateConversationNotExistsBetweenAccounts(String participantsKey) {
        Optional<ConversationDto> optionalConversationDto = conversationRetrieveService.retrieveWithParticipantsKey(participantsKey);
        optionalConversationDto
                .map(ConversationDto::getConversationId)
                .map(existingConversationId -> new BusinessException("messaging.conversation.exists-with-participants", Map.of("existingConversationId", existingConversationId)))
                .ifPresent(businessException -> {
                    throw businessException;
                });
    }
}
