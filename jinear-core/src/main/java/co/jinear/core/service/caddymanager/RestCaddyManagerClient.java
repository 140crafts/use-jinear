package co.jinear.core.service.caddymanager;

import co.jinear.core.config.properties.CaddyManagerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.caddymanager", havingValue = "false")
public class RestCaddyManagerClient implements CaddyManagerClient {

    private static final String AUTH_HEADER = "Bearer %s";
    private static final String CONFIG = "/config";

    private final CaddyManagerProperties caddyManagerProperties;
    private final RestTemplate caddyManagerRestTemplate;

    @Override
    public boolean updateConfig(List<String> domainNameList) {
        log.info("Update config has started. size: {}", domainNameList.size());
        Map<String, Object> body = new HashMap<>();
        body.put("hosts", domainNameList);
        HttpHeaders headers = retrieveHeaders(caddyManagerProperties.getToken());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> responseEntity = caddyManagerRestTemplate.exchange(CONFIG, HttpMethod.PUT, requestEntity, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    private HttpHeaders retrieveHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER.formatted(token));
        return headers;
    }
}
