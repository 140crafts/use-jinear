package co.jinear.core.controller.payments;

import co.jinear.core.manager.payments.PaymentsManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/payments/process")
public class PaymentProcessController {

    private static final int WAIT_FOR_CALLBACK_COMPLETION_SECONDS = 2;
    private final PaymentsManager paymentsManager;

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse process() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(WAIT_FOR_CALLBACK_COMPLETION_SECONDS));
        return paymentsManager.retrieveAndApplyLatestPayments();
    }
}
