package co.jinear.core.service.client.paymentprocessor;

import co.jinear.core.service.client.paymentprocessor.model.response.RetrieveSubscriptionInfoResponse;
import co.jinear.core.service.client.paymentprocessor.model.response.purchase.PurchaseListingResponse;

public interface PaymentProcessorClient {

    PurchaseListingResponse retrievePurchasesAfter(String product, String afterIsoDate);

    RetrieveSubscriptionInfoResponse retrieveSubscriptionInfo(String subscriptionId);
}
