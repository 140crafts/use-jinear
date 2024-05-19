package co.jinear.core.manager.messaging;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.request.messaging.thread.InitializeThreadRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ThreadListingResponse;
import co.jinear.core.model.response.messaging.ThreadResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.channel.ChannelMemberOperationService;
import co.jinear.core.service.messaging.channel.ChannelNotifierService;
import co.jinear.core.service.messaging.message.MessageRetrieveService;
import co.jinear.core.service.messaging.thread.ThreadListingService;
import co.jinear.core.service.messaging.thread.ThreadOperationService;
import co.jinear.core.service.messaging.thread.ThreadRetrieveService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadManager {

    private final ThreadOperationService threadOperationService;
    private final SessionInfoService sessionInfoService;
    private final ChannelAccessValidator channelAccessValidator;
    private final ThreadListingService threadListingService;
    private final ChannelNotifierService channelNotifierService;
    private final ThreadRetrieveService threadRetrieveService;
    private final ChannelMemberOperationService channelMemberOperationService;
    private final MessageRetrieveService messageRetrieveService;

    public BaseResponse initializeThread(InitializeThreadRequest initializeThreadRequest) {
        String accountId = sessionInfoService.currentAccountId();
        String channelId = initializeThreadRequest.getChannelId();
        String initialMessageBody = initializeThreadRequest.getInitialMessageBody();
        channelAccessValidator.validateChannelParticipationAccess(accountId, channelId);
        log.info("Initialize thread has started. accountId: {}, initializeThreadRequest: {}", accountId, initializeThreadRequest);
        RichMessageDto saved = threadOperationService.initializeThread(accountId, channelId, initialMessageBody);
        channelNotifierService.notifyChannelMembers(channelId, saved.getMessageId());
        return new BaseResponse();
    }

    public ThreadResponse retrieve(String threadId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve thread has started. currentAccountId: {}", currentAccountId);
        ThreadDto threadDto = threadRetrieveService.retrieve(threadId);
        channelAccessValidator.validateChannelAccess(threadDto.getChannelId(), currentAccountId);
        return mapResponse(threadDto);
    }

    public ThreadListingResponse listThreads(String channelId, ZonedDateTime before) {
        String currentAccountId = sessionInfoService.currentAccountId();
        channelAccessValidator.validateChannelAccess(channelId, currentAccountId);
        log.info("List threads has started. currentAccountId: {}, before: {}", currentAccountId, before);
        Page<ThreadDto> threadPage = threadListingService.listThreads(channelId, before);
        channelMemberOperationService.updateLastCheck(channelId, currentAccountId, ZonedDateTime.now());
        return mapResponse(threadPage);
    }

    private ThreadListingResponse mapResponse(Page<ThreadDto> page) {
        ThreadListingResponse threadListingResponse = new ThreadListingResponse();
        threadListingResponse.setThreadsPage(new PageDto<>(page));
        return threadListingResponse;
    }

    private ThreadResponse mapResponse(ThreadDto threadDto) {
        ThreadResponse threadResponse = new ThreadResponse();
        threadResponse.setThreadDto(threadDto);
        return threadResponse;
    }
}
