package co.jinear.core.service.messaging.message;

import co.jinear.core.repository.messaging.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListingService {

    private final MessageRepository messageRepository;

//    public Page<Message>
}
