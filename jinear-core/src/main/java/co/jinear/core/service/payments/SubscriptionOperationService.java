package co.jinear.core.service.payments;

import co.jinear.core.model.dto.payments.SubscriptionDto;
import co.jinear.core.model.entity.payments.Subscription;
import co.jinear.core.repository.SubscriptionRepository;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionOperationService {

    private final SubscriptionRepository subscriptionRepository;

    public void initializeSubscription(SubscriptionDto subscriptionDto) {
        log.info("Initialize subscription has started. subscriptionDto: {}", subscriptionDto);
        Subscription subscription = new Subscription();
        subscription.setPaymentsServiceSubscriptionId(subscriptionDto.getPaymentsServiceSubscriptionId());
        subscription.setSubscriptionStatus(subscriptionDto.getSubscriptionStatus());
        subscription.setRelatedObjectId(subscriptionDto.getRelatedObjectId());
        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Initialize subscription has completed. subscriptionId: {}", saved.getSubscriptionId());
    }

    public void updateSubscriptionStatus(Subscription subscription, SubscriptionStatus subscriptionStatus) {
        log.info("Update subscription status has started. subscriptionId: {}, subscriptionStatus: {}", subscription.getSubscriptionId(), subscriptionStatus);
        subscription.setSubscriptionStatus(subscriptionStatus);
        subscriptionRepository.save(subscription);
        log.info("Update subscription status has completed.");
    }
}
