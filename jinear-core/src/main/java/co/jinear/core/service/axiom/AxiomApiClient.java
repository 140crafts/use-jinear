package co.jinear.core.service.axiom;

import co.jinear.core.service.axiom.request.AxiomIngestRequest;

import java.util.List;

public interface AxiomApiClient {

    void ingest(List<AxiomIngestRequest> axiomIngestRequest);
}
