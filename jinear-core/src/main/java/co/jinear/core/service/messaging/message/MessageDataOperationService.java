package co.jinear.core.service.messaging.message;

import co.jinear.core.model.entity.messaging.MessageData;
import co.jinear.core.repository.messaging.MessageDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDataOperationService {

    private final MessageDataRepository messageDataRepository;

    public void initializeAll(String messageId, Map<String, String> data) {
        if (data.isEmpty()) {
            return;
        }
        log.info("Initialize all message data has started. messageId: {}", messageId);
        data.forEach((key, val) -> initialize(messageId, key, val));
    }

    private void initialize(String messageId, String key, String val) {
        MessageData messageData = new MessageData();
        messageData.setDataKey(key);
        messageData.setDataValue(val);
        messageData.setMessageId(messageId);
        messageDataRepository.save(messageData);
    }
}
