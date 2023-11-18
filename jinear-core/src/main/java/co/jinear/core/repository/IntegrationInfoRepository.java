package co.jinear.core.repository;

import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntegrationInfoRepository extends JpaRepository<IntegrationInfo, String> {

    Optional<IntegrationInfo> findByAccountIdAndProviderAndPassiveIdIsNull(String accountId, IntegrationProvider provider);
}
