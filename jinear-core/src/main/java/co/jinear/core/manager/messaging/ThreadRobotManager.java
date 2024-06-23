package co.jinear.core.manager.messaging;

import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.enumtype.messaging.ThreadType;
import co.jinear.core.model.request.messaging.thread.InitializeThreadRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.channel.ChannelNotifierService;
import co.jinear.core.service.messaging.thread.ThreadOperationService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadRobotManager {

    private final SessionInfoService sessionInfoService;
    private final ChannelAccessValidator channelAccessValidator;
    private final ThreadOperationService threadOperationService;
    private final ChannelNotifierService channelNotifierService;

    public BaseResponse initializeThread(InitializeThreadRequest initializeThreadRequest) {
        String robotId = sessionInfoService.currentAccountId();
        String channelId = initializeThreadRequest.getChannelId();
        String initialMessageBody = initializeThreadRequest.getInitialMessageBody();
        channelAccessValidator.validateRobotChannelParticipationAccess(robotId, channelId);
        log.info("Initialize thread has started. robotId: {}, initializeThreadRequest: {}", robotId, initializeThreadRequest);
        RichMessageDto saved = threadOperationService.initializeThread(robotId, channelId, initialMessageBody, ThreadType.INITIALIZED_BY_ROBOT);
        channelNotifierService.notifyChannelMembers(channelId, saved.getMessageId());
        return new BaseResponse();
    }

}
