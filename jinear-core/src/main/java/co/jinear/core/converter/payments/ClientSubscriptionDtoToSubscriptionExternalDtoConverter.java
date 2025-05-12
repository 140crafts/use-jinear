package co.jinear.core.converter.payments;

import co.jinear.core.model.dto.payments.SubscriptionExternalDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientSubscriptionDtoToSubscriptionExternalDtoConverter {

    public SubscriptionExternalDto convert(co.jinear.core.service.client.paymentprocessor.model.dto.subscription.SubscriptionDto clientSubscriptionDto) {
        log.info("Convert client subscriptionDto to subscriptionDto has started. clientSubscriptionDto: {}", clientSubscriptionDto);
        SubscriptionExternalDto subscriptionExternalDto = new SubscriptionExternalDto();
        subscriptionExternalDto.setPaymentsServiceSubscriptionId(clientSubscriptionDto.getSubscriptionId());
        subscriptionExternalDto.setSubscriptionStatus(clientSubscriptionDto.getSubscriptionStatus());
        subscriptionExternalDto.setPassthroughDetails(clientSubscriptionDto.getPassthroughDetails());
        return subscriptionExternalDto;
    }
}
