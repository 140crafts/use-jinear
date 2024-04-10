package co.jinear.core.service.messaging.message;

import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.repository.messaging.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListingService {

    private static final int PAGE_SIZE = 100;

    private final MessageRepository messageRepository;
    private final MessageDtoConverter messageDtoConverter;

    public Page<MessageDto> retrieveThreadMessagesBefore(String threadId, Date before) {
        log.info("Retrieve thread messages before has started. threadId: {}, before: {}", threadId, before);
        return messageRepository.findAllByThreadIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateAsc(threadId, before, PageRequest.of(0, PAGE_SIZE))
                .map(messageDtoConverter::convert);
    }
}
