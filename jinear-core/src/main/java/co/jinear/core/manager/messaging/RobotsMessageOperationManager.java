package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.message.SendMessageRequestConverter;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.service.SessionInfoService;
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
public class RobotsMessageOperationManager {

    private final SessionInfoService sessionInfoService;
    private final ThreadAccessValidator threadAccessValidator;
    private final SendMessageRequestConverter sendMessageRequestConverter;
    private final MessageOperationService messageOperationService;
    private final MessageRetrieveService messageRetrieveService;
    private final ThreadNotifierService threadNotifierService;

    public BaseResponse sendToThread(String threadId, SendMessageRequest sendMessageRequest) {
        String robotId = sessionInfoService.currentAccountId();
        threadAccessValidator.validateRobotThreadAccess(threadId, robotId);
        log.info("Send to thread has started. robotId: {}, threadId: {}", robotId, threadId);
        InitializeMessageVo initializeMessageVo = sendMessageRequestConverter.convertForThreadRobot(robotId, threadId, sendMessageRequest);
        RichMessageDto saved = messageOperationService.initialize(initializeMessageVo);
        RichMessageDto richMessageDto = messageRetrieveService.retrieveRich(saved.getMessageId());
        threadNotifierService.notifyThreadParticipants(richMessageDto);
        return new BaseResponse();
    }
}
