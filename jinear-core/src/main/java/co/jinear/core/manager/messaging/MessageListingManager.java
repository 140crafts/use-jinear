package co.jinear.core.manager.messaging;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.response.messaging.MessageListingPaginatedResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.message.MessageListingService;
import co.jinear.core.validator.messaging.thread.ThreadAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListingManager {

    private final SessionInfoService sessionInfoService;
    private final MessageListingService messageListingService;
    private final ThreadAccessValidator threadAccessValidator;

    public MessageListingPaginatedResponse listThreadMessagesBefore(String threadId, ZonedDateTime before) {
        String currentAccountId = sessionInfoService.currentAccountId();
        threadAccessValidator.validateThreadAccess(threadId, currentAccountId);
        Date beforeParsed = Date.from(before.toInstant());
        log.info("List thread messages before has started. threadId: {}, before: {}, beforeParsed: {}", threadId, before, beforeParsed);
        Page<MessageDto> messageDtoPage = messageListingService.retrieveThreadMessagesBefore(threadId, beforeParsed);
        return mapResponse(messageDtoPage);
    }

    private MessageListingPaginatedResponse mapResponse(Page<MessageDto> messageDtoPage) {
        MessageListingPaginatedResponse messageListingPaginatedResponse = new MessageListingPaginatedResponse();
        messageListingPaginatedResponse.setMessageDtoPage(new PageDto<>(messageDtoPage));
        return messageListingPaginatedResponse;
    }
}
