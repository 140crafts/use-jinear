package co.jinear.core.service.client.paymentprocessor;

import co.jinear.core.service.client.paymentprocessor.model.enumtype.ProductType;
import co.jinear.core.service.client.paymentprocessor.model.response.purchase.PurchaseListingResponse;

public interface PaymentProcessorClient {

    PurchaseListingResponse retrievePurchasesAfter(ProductType product, String afterIsoDate);
}
