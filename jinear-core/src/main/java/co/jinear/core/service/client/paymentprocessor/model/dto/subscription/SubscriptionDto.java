package co.jinear.core.service.client.paymentprocessor.model.dto.subscription;

import co.jinear.core.service.client.paymentprocessor.model.dto.BaseDto;
import co.jinear.core.service.client.paymentprocessor.model.dto.PassthroughDetailDto;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.PaymentProviderType;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.ProductType;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class SubscriptionDto extends BaseDto {

    private String subscriptionId;
    private String externalId;
    private PaymentProviderType provider;
    private ProductType product;
    private SubscriptionStatus subscriptionStatus;
    private Set<PassthroughDetailDto> passthroughDetails;
}
