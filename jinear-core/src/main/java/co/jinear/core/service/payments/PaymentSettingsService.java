package co.jinear.core.service.payments;

import co.jinear.core.model.entity.payments.PaymentSettings;
import co.jinear.core.repository.PaymentSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSettingsService {

    private final PaymentSettingsRepository paymentSettingsRepository;

    public PaymentSettings retrieveSettings() {
        log.info("Retrieve settings has started.");
        return paymentSettingsRepository.findAll().stream()
                .findFirst()
                .orElseGet(this::initializeAndRetrieveFirstPaymentSettings);
    }

    public void updateLastSyncDate(ZonedDateTime zonedDateTime) {
        log.info("Update last sync date has started. zonedDateTime: {}", zonedDateTime);
        PaymentSettings paymentSettings = retrieveSettings();
        paymentSettings.setLastSync(zonedDateTime);
        paymentSettingsRepository.save(paymentSettings);
        log.info("Update last sync date has completed.");
    }

    public ZonedDateTime retrieveLastSyncDate() {
        log.info("Retrieve last sync date has started.");
        PaymentSettings paymentSettings = retrieveSettings();
        return paymentSettings.getLastSync();
    }

    private PaymentSettings initializeAndRetrieveFirstPaymentSettings() {
        log.info("Initializing first payment settings.");
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.setLastSync(ZonedDateTime.now().minusYears(99));
        return paymentSettingsRepository.save(paymentSettings);
    }
}
