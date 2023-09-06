package co.jinear.core.service.client.paymentprocessor.model.dto.purchase;

import co.jinear.core.service.client.paymentprocessor.model.dto.oneoffpurchase.OneOffPurchaseDto;
import co.jinear.core.service.client.paymentprocessor.model.dto.subscription.SubscriptionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseListingDto {

    private List<OneOffPurchaseDto> oneOffPurchaseDtoList;
    private List<SubscriptionDto> subscriptionDtoList;
}
