package co.jinear.core.validator.messaging.thread;

import co.jinear.core.model.dto.messaging.thread.PlainThreadDto;
import co.jinear.core.service.messaging.thread.ThreadRetrieveService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThreadAccessValidator {

    private final ThreadRetrieveService threadRetrieveService;
    private final ChannelAccessValidator channelAccessValidator;

    public void validateThreadAccess(String threadId, String accountId) {
        log.info("Validate thread access has started. threadId: {}, accountId: {}", threadId, accountId);
        PlainThreadDto plainThreadDto = threadRetrieveService.retrievePlain(threadId);
        channelAccessValidator.validateChannelAccess(plainThreadDto.getChannelId(), accountId);
    }
}
