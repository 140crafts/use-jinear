package co.jinear.core.manager.messaging;

import co.jinear.core.model.request.messaging.thread.InitializeThreadRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.thread.ThreadOperationService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadManager {

    private final ThreadOperationService threadOperationService;
    private final SessionInfoService sessionInfoService;
    private final ChannelAccessValidator channelAccessValidator;

    public BaseResponse initializeThread(InitializeThreadRequest initializeThreadRequest) {
        String accountId = sessionInfoService.currentAccountId();
        String channelId = initializeThreadRequest.getChannelId();
        String initialMessageBody = initializeThreadRequest.getInitialMessageBody();
        channelAccessValidator.validateChannelAdminAccess(accountId, channelId);
        log.info("Initialize thread has started. accountId: {}, initializeThreadRequest: {}", accountId, initializeThreadRequest);
        threadOperationService.initializeThread(accountId, channelId, initialMessageBody);
        return new BaseResponse();
    }
}
