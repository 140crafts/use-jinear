package co.jinear.core.service.integration;

import co.jinear.core.model.entity.integration.IntegrationScope;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.repository.IntegrationScopeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationScopeOperationService {

    private final IntegrationScopeRepository integrationScopeRepository;

    public void initializeScope(String integrationInfoId, IntegrationScopeType scope) {
        log.info("Initialize integration scope has started. integrationInfoId: {}, scope: {}", integrationInfoId, scope);
        IntegrationScope integrationScope = mapToEntity(integrationInfoId, scope);
        integrationScopeRepository.save(integrationScope);
        log.info("Initialize integration scope has completed.");
    }

    @NonNull
    private static IntegrationScope mapToEntity(String integrationInfoId, IntegrationScopeType scope) {
        IntegrationScope integrationScope = new IntegrationScope();
        integrationScope.setIntegrationInfoId(integrationInfoId);
        integrationScope.setScope(scope);
        return integrationScope;
    }
}
