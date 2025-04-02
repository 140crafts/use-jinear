package co.jinear.core.manager.payments;

import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.payments.PaymentSettingsService;
import co.jinear.core.service.payments.PaymentsOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentsManager {

    private final PaymentsOperationService paymentsOperationService;
    private final PaymentSettingsService paymentSettingsService;

    @Scheduled(fixedRate = 10000)
    public BaseResponse retrieveAndApplyLatestPayments() {
        log.info("Retrieve and apply latest payments has started.");
        ZonedDateTime lastSyncDate = paymentSettingsService.retrieveLastSyncDate();
        paymentsOperationService.retrieveAndApplyLatestPayments(lastSyncDate.minusHours(1));
        paymentSettingsService.updateLastSyncDate(ZonedDateTime.now());
        return new BaseResponse();
    }
}
