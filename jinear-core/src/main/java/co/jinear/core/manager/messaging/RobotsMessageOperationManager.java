package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.message.SendMessageRequestConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.captcha.CaptchaResolveVo;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.captcha.CaptchaChallengeService;
import co.jinear.core.service.messaging.message.MessageOperationService;
import co.jinear.core.service.messaging.message.MessageRetrieveService;
import co.jinear.core.service.messaging.thread.ThreadNotifierService;
import co.jinear.core.system.IpResolver;
import co.jinear.core.validator.messaging.thread.ThreadAccessValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    private final CaptchaChallengeService captchaChallengeService;

    public BaseResponse sendToThread(String threadId, SendMessageRequest sendMessageRequest, HttpServletRequest httpServletRequest) {
        String robotId = sessionInfoService.currentAccountId();
        captchaChallengeService.verifySolution(httpServletRequest, sendMessageRequest.getCaptchaResolveVos());
        threadAccessValidator.validateRobotThreadAccess(threadId, robotId);
        log.info("Send to thread has started. robotId: {}, threadId: {}", robotId, threadId);
        InitializeMessageVo initializeMessageVo = sendMessageRequestConverter.convertForThreadRobot(robotId, threadId, sendMessageRequest);
        RichMessageDto saved = messageOperationService.initialize(initializeMessageVo);
        RichMessageDto richMessageDto = messageRetrieveService.retrieveRich(saved.getMessageId());
        threadNotifierService.notifyThreadParticipants(richMessageDto);
        return new BaseResponse();
    }
}
