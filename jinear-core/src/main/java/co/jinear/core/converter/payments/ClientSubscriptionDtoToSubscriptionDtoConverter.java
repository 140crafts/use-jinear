package co.jinear.core.converter.payments;

import co.jinear.core.model.dto.payments.SubscriptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientSubscriptionDtoToSubscriptionDtoConverter {

    public SubscriptionDto convert(co.jinear.core.service.client.paymentprocessor.model.dto.subscription.SubscriptionDto clientSubscriptionDto) {
        log.info("Covnert client subscriptionDto to subscriptionDto has started. clientSubscriptionDto: {}", clientSubscriptionDto);
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setPaymentsServiceSubscriptionId(clientSubscriptionDto.getSubscriptionId());
        subscriptionDto.setSubscriptionStatus(clientSubscriptionDto.getSubscriptionStatus());
        subscriptionDto.setRelatedObjectId(clientSubscriptionDto.getRelatedObjectId());
        return subscriptionDto;
    }
}
