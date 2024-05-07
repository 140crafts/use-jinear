package co.jinear.core.service.messaging.message;

import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.repository.messaging.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageRetrieveService {

    private final MessageRepository messageRepository;
    private final MessageDtoConverter messageDtoConverter;

    public RichMessageDto retrieveRich(String messageId) {
        log.info("Retrieve rich message has started. messageId: {}", messageId);
        return messageRepository.findByMessageIdAndPassiveIdIsNull(messageId)
                .map(messageDtoConverter::convertRich)
                .orElseThrow(NotFoundException::new);
    }
}
