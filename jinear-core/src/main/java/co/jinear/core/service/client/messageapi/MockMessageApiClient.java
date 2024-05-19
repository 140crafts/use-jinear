package co.jinear.core.service.client.messageapi;

import co.jinear.core.service.client.messageapi.model.request.EmitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "mock.message-api-client.enabled", havingValue = "true")
public class MockMessageApiClient implements MessageApiClient {

    @Override
    public void emit(EmitRequest emitRequest) {
        log.info("[MOCK] Emit request has started. emitRequest: {}", emitRequest);
    }
}
