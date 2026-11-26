package co.jinear.core.service.client.paymentprocessor;

import co.jinear.core.service.client.paymentprocessor.model.response.RetrieveSubscriptionInfoResponse;
import co.jinear.core.service.client.paymentprocessor.model.response.purchase.PurchaseListingResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface PaymentProcessorHttpClient extends PaymentProcessorClient {

    @GetExchange("/purchase-listing/{product}/{after}")
    PurchaseListingResponse retrievePurchasesAfter(@PathVariable String product,
                                                   @PathVariable String after);

    @GetExchange("/subscription/info/{subscriptionId}")
    RetrieveSubscriptionInfoResponse retrieveSubscriptionInfo(@PathVariable String subscriptionId);
}
