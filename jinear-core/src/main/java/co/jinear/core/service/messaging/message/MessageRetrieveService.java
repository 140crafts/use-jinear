package co.jinear.core.service.messaging.message;

import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.repository.messaging.MessageRepository;
import co.jinear.core.service.messaging.thread.ThreadRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageRetrieveService {

    private final MessageRepository messageRepository;
    private final MessageDtoConverter messageDtoConverter;
    private final ThreadRetrieveService threadRetrieveService;

    public RichMessageDto retrieveRich(String messageId) {
        log.info("Retrieve rich message has started. messageId: {}", messageId);
        return messageRepository.findByMessageIdAndPassiveIdIsNull(messageId)
                .map(messageDtoConverter::convertRich)
                .map(this::retrieveAndMapThread)
                .orElseThrow(NotFoundException::new);
    }

    private RichMessageDto retrieveAndMapThread(RichMessageDto richMessageDto) {
        log.info("Retrieve and map thread has started.");
        String threadId = richMessageDto.getThreadId();
        ThreadDto threadDto = threadRetrieveService.retrieve(threadId);
        richMessageDto.setThread(threadDto);
        return richMessageDto;
    }
}
