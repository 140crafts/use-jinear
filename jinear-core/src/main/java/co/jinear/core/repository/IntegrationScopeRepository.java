package co.jinear.core.repository;

import co.jinear.core.model.entity.integration.IntegrationScope;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntegrationScopeRepository extends JpaRepository<IntegrationScope, String> {

    Optional<IntegrationScope> findByIntegrationInfo_AccountIdAndIntegrationInfo_ProviderAndIntegrationInfo_RelatedObjectIdAndIntegrationInfo_PassiveIdIsNullAndScopeAndPassiveIdIsNull(String accountId, IntegrationProvider provider, String relatedObjectId, IntegrationScopeType scope);
}
