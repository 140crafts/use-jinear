package co.jinear.core.service.client.messageapi;

import co.jinear.core.config.properties.MessageApiProperties;
import co.jinear.core.service.client.messageapi.model.request.EmitRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.message-api-client.enabled", havingValue = "false", matchIfMissing = true)
public class RestMessageApiClient implements MessageApiClient {

    private static final String AUTH_HEADER = "Bearer %s";
    private static final String EMIT = "/emit";

    private final MessageApiProperties messageApiProperties;
    private final RestTemplate messageApiRestTemplate;

    @Override
    public void emit(EmitRequest emitRequest) {
        log.info("Emit request has started. emitRequest: {}", emitRequest);
        HttpHeaders headers = retrieveHeaders(messageApiProperties.getToken());
        HttpEntity<EmitRequest> requestEntity = new HttpEntity<>(emitRequest, headers);
        messageApiRestTemplate.exchange(EMIT, HttpMethod.POST, requestEntity, Void.class);
    }

    private HttpHeaders retrieveHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER.formatted(token));
        return headers;
    }
}
