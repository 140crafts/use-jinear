package co.jinear.core.service.axiom;

import co.jinear.core.service.axiom.request.AxiomIngestRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.axiom", havingValue = "true")
public class AxiomApiMockClient implements AxiomApiClient {

    @Override
    public void ingest(List<AxiomIngestRequest> axiomIngestRequest) {
        log.info("[MOCK] Axiom ingest has started. axiomIngestRequest");
    }
}
