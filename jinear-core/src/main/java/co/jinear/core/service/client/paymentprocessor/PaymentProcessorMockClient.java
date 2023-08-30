package co.jinear.core.service.client.paymentprocessor;

import co.jinear.core.service.client.paymentprocessor.model.dto.purchase.PurchaseListingDto;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.ProductType;
import co.jinear.core.service.client.paymentprocessor.model.response.purchase.PurchaseListingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@ConditionalOnProperty(value = "payment-processor.mock", havingValue = "true", matchIfMissing = true)
public class PaymentProcessorMockClient implements PaymentProcessorClient {

    @Override
    public PurchaseListingResponse retrievePurchasesAfter(ProductType product, String afterIsoDate) {
        log.info("[MOCK] Retrieve purchases after has started. product: {}, after: {}", product, afterIsoDate);
        PurchaseListingDto purchaseListingDto = new PurchaseListingDto();

        purchaseListingDto.setOneOffPurchaseDtoList(Collections.emptyList());
        purchaseListingDto.setSubscriptionDtoList(Collections.emptyList());

        PurchaseListingResponse purchaseListingResponse = new PurchaseListingResponse();
        purchaseListingResponse.setPurchaseListingDto(purchaseListingDto);

        return purchaseListingResponse;
    }
}
