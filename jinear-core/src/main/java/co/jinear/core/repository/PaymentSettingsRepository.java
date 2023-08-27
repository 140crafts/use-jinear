package co.jinear.core.repository;

import co.jinear.core.model.entity.payments.PaymentSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSettingsRepository extends JpaRepository<PaymentSettings,String> {
}
