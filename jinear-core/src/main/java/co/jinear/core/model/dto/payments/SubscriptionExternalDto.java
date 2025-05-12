package co.jinear.core.model.dto.payments;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.service.client.paymentprocessor.model.dto.PassthroughDetailDto;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class SubscriptionExternalDto extends BaseDto {

    private String subscriptionId;
    private String paymentsServiceSubscriptionId;
    private SubscriptionStatus subscriptionStatus;
    private Set<PassthroughDetailDto> passthroughDetails;
}
