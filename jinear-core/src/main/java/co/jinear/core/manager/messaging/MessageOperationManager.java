package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.message.SendMessageRequestConverter;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.response.messaging.MessageResponse;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.conversation.ConversationNotifierService;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantRetrieveService;
import co.jinear.core.service.messaging.message.MessageOperationService;
import co.jinear.core.service.messaging.message.MessageRetrieveService;
import co.jinear.core.service.messaging.thread.ThreadNotifierService;
import co.jinear.core.validator.messaging.thread.ThreadAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageOperationManager {

    private final MessageOperationService messageOperationService;
    private final SessionInfoService sessionInfoService;
    private final SendMessageRequestConverter sendMessageRequestConverter;
    private final ThreadAccessValidator threadAccessValidator;
    private final ConversationParticipantRetrieveService conversationParticipantRetrieveService;
    private final ThreadNotifierService threadNotifierService;
    private final ConversationNotifierService conversationNotifierService;
    private final MessageRetrieveService messageRetrieveService;

    public MessageResponse sendToThread(String threadId, SendMessageRequest sendMessageRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        threadAccessValidator.validateThreadAccess(threadId, currentAccountId);
        log.info("Send to thread has started. currentAccountId: {}, threadId: {}", currentAccountId, threadId);
        InitializeMessageVo initializeMessageVo = sendMessageRequestConverter.convertForThread(currentAccountId, threadId, sendMessageRequest);
        RichMessageDto saved = messageOperationService.initialize(initializeMessageVo);
        RichMessageDto richMessageDto = messageRetrieveService.retrieveRich(saved.getMessageId());
        threadNotifierService.notifyThreadParticipants(richMessageDto);
        return mapResponse(richMessageDto);
    }

    public MessageResponse sendToConversation(String conversationId, SendMessageRequest sendMessageRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        conversationParticipantRetrieveService.validateParticipantIsInConversation(currentAccountId, conversationId);
        log.info("Send to conversation has started. currentAccountId: {}, conversationId: {}", currentAccountId, conversationId);
        InitializeMessageVo initializeMessageVo = sendMessageRequestConverter.convertForConversation(currentAccountId, conversationId, sendMessageRequest);
        RichMessageDto saved = messageOperationService.initialize(initializeMessageVo);
        RichMessageDto richMessageDto = messageRetrieveService.retrieveRich(saved.getMessageId());
        conversationNotifierService.notify(richMessageDto);
        return mapResponse(richMessageDto);
    }

    private MessageResponse mapResponse(RichMessageDto message) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessageDto(message);
        return messageResponse;
    }
}
