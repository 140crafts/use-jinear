package co.jinear.core.service.messaging.emit;

import co.jinear.core.service.client.messageapi.MessageApiClient;
import co.jinear.core.service.client.messageapi.model.request.EmitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmitterService {

    private final MessageApiClient messageApiClient;

    @Async
    public void emitMessage(EmitRequest emitRequest) {
        log.info("Emit message has started.");
        messageApiClient.emit(emitRequest);
        log.info("Emit message has completed.");
    }
}
