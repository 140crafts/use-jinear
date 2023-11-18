package co.jinear.core.service.integration;

import co.jinear.core.converter.integration.IntegrationScopeDtoConverter;
import co.jinear.core.model.dto.integration.IntegrationScopeDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.repository.IntegrationScopeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationScopeRetrieveService {

    private final IntegrationScopeRepository integrationScopeRepository;
    private final IntegrationScopeDtoConverter integrationScopeDtoConverter;

    public Optional<IntegrationScopeDto> retrieveIntegrationWithScope(String accountId, IntegrationProvider provider, IntegrationScopeType scope) {
        log.info("Retrieve integration with scope and provider has started. accountId: {}, provider: {}, scope: {}", accountId, provider, scope);
        return integrationScopeRepository.findByIntegrationInfo_AccountIdAndIntegrationInfo_ProviderAndIntegrationInfo_PassiveIdIsNullAndScopeAndPassiveIdIsNull(accountId, provider, scope)
                .map(integrationScopeDtoConverter::map);
    }
}
