package co.jinear.core.service.integration;

import co.jinear.core.converter.integration.IntegrationScopeDtoConverter;
import co.jinear.core.model.dto.integration.IntegrationScopeDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.repository.IntegrationScopeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationScopeRetrieveService {

    private final IntegrationScopeRepository integrationScopeRepository;
    private final IntegrationScopeDtoConverter integrationScopeDtoConverter;

    public Optional<IntegrationScopeDto> retrieveIntegrationWithScope(String accountId, IntegrationProvider provider, String relatedObjectId, IntegrationScopeType scope) {
        log.info("Retrieve integration with scope and provider has started. accountId: {}, provider: {}, relatedObjectId: {}, scope: {}", accountId, provider, scope, relatedObjectId);
        return integrationScopeRepository.findByIntegrationInfo_AccountIdAndIntegrationInfo_ProviderAndIntegrationInfo_RelatedObjectIdAndIntegrationInfo_PassiveIdIsNullAndScopeAndPassiveIdIsNull(accountId, provider, relatedObjectId, scope)
                .map(integrationScopeDtoConverter::map);
    }

    public List<IntegrationScopeDto> retrieveIntegrationsWithScope(String accountId, IntegrationScopeType scope) {
        log.info("Retrieve integrations with scope has started. accountId: {}, scope: {}", accountId, scope);
        return integrationScopeRepository.findAllByIntegrationInfo_AccountIdAndIntegrationInfo_PassiveIdIsNullAndScopeAndPassiveIdIsNull(accountId, scope)
                .stream()
                .map(integrationScopeDtoConverter::map)
                .toList();
    }
}
