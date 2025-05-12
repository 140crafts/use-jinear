package co.jinear.core.repository;

import co.jinear.core.model.entity.payments.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Optional<Subscription> findByPaymentsServiceSubscriptionIdAndPassiveIdIsNull(String paymentsServiceSubscriptionId);

    Optional<Subscription> findByWorkspaceIdAndPassiveIdIsNull(String workspaceId);
}
