package co.jinear.core.controller.payments;

import co.jinear.core.manager.payments.PaymentsInfoManager;
import co.jinear.core.model.response.payments.RetrieveSubscriptionInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/payments/info")
public class PaymentInfoController {

    private final PaymentsInfoManager paymentsInfoManager;

    @GetMapping("/workspace/{workspaceId}/subscription")
    @ResponseStatus(HttpStatus.OK)
    public RetrieveSubscriptionInfoResponse retrieveSubscriptionInfo(@PathVariable String workspaceId) {
        return paymentsInfoManager.retrieveSubscriptionInfo(workspaceId);
    }
}
