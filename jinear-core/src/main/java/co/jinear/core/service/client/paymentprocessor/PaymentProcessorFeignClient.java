package co.jinear.core.service.client.paymentprocessor;

import co.jinear.core.config.client.paymentprocessor.PaymentProcessorApiClientConfig;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.ProductType;
import co.jinear.core.service.client.paymentprocessor.model.response.purchase.PurchaseListingResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.ZonedDateTime;

@FeignClient(
        url = "${payment-processor.url}",
        configuration = PaymentProcessorApiClientConfig.class
)
@ConditionalOnProperty(value = "payment-processor.mock", havingValue = "false")
public interface PaymentProcessorFeignClient extends PaymentProcessorClient {

    @PostMapping("/purchase-listing/{product}/{after}")
    PurchaseListingResponse retrievePurchasesAfter(@PathVariable ProductType product,
                                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime after);
}
