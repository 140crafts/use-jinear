package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.message.SendMessageRequestConverter;
import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.message.MessageOperationService;
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

    public BaseResponse sendToThread(String threadId, SendMessageRequest sendMessageRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        threadAccessValidator.validateThreadAccess(threadId, currentAccountId);
        log.info("Send to thread has started. currentAccountId: {}, threadId: {}", currentAccountId, threadId);
        InitializeMessageVo initializeMessageVo = sendMessageRequestConverter.convertForThread(currentAccountId, threadId, sendMessageRequest);
        messageOperationService.initialize(initializeMessageVo);
        return new BaseResponse();
    }
}
