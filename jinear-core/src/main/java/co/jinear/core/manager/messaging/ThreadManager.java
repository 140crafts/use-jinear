package co.jinear.core.manager.messaging;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.request.messaging.thread.InitializeThreadRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ThreadListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.channel.ChannelNotifierService;
import co.jinear.core.service.messaging.thread.ThreadListingService;
import co.jinear.core.service.messaging.thread.ThreadOperationService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadManager {

    private final ThreadOperationService threadOperationService;
    private final SessionInfoService sessionInfoService;
    private final ChannelAccessValidator channelAccessValidator;
    private final ThreadListingService threadListingService;
    private final ChannelNotifierService channelNotifierService;

    public BaseResponse initializeThread(InitializeThreadRequest initializeThreadRequest) {
        String accountId = sessionInfoService.currentAccountId();
        String channelId = initializeThreadRequest.getChannelId();
        String initialMessageBody = initializeThreadRequest.getInitialMessageBody();
        channelAccessValidator.validateChannelParticipationAccess(accountId, channelId);
        log.info("Initialize thread has started. accountId: {}, initializeThreadRequest: {}", accountId, initializeThreadRequest);
        RichMessageDto firstMessage = threadOperationService.initializeThread(accountId, channelId, initialMessageBody);
        channelNotifierService.notifyChannelMembers(channelId, firstMessage);
        return new BaseResponse();
    }

    public ThreadListingResponse listThreads(String channelId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        channelAccessValidator.validateChannelAccess(channelId, currentAccountId);
        log.info("List threads has started. currentAccountId: {}", currentAccountId);
        Page<ThreadDto> threadPage = threadListingService.listThreads(channelId, page);
        return mapResponse(threadPage);
    }

    private ThreadListingResponse mapResponse(Page<ThreadDto> page) {
        ThreadListingResponse threadListingResponse = new ThreadListingResponse();
        threadListingResponse.setThreadsPage(new PageDto<>(page));
        return threadListingResponse;
    }
}
