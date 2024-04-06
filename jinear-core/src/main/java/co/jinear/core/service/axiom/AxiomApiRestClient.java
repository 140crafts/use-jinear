package co.jinear.core.service.axiom;

import co.jinear.core.config.properties.AxiomProperties;
import co.jinear.core.service.axiom.request.AxiomIngestRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.axiom", havingValue = "false")
public class AxiomApiRestClient implements AxiomApiClient {

    private static final String AUTH_HEADER = "Bearer %s";
    private static final String INGEST = "/datasets/%s/ingest";

    private final RestTemplate axiomRestTemplate;
    private final AxiomProperties axiomProperties;

    @Override
    public void ingest(List<AxiomIngestRequest> axiomIngestRequestList) {
        HttpHeaders headers = retrieveHeaders(axiomProperties.getToken());
        HttpEntity<List<AxiomIngestRequest>> requestEntity = new HttpEntity<>(axiomIngestRequestList, headers);
        axiomRestTemplate.exchange(INGEST.formatted(axiomProperties.getDataset()), HttpMethod.POST, requestEntity, Void.class);
    }

    private HttpHeaders retrieveHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER.formatted(token));
        return headers;
    }
}
