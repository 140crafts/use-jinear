package co.jinear.core.service.payments;

import co.jinear.core.model.dto.payments.SubscriptionExternalDto;
import co.jinear.core.model.entity.payments.Subscription;
import co.jinear.core.repository.SubscriptionRepository;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.PassthroughType;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import co.jinear.core.system.util.payment.PassthroughHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionOperationService {

    private final SubscriptionRepository subscriptionRepository;

    public void initializeSubscription(SubscriptionExternalDto subscriptionExternalDto) {
        log.info("Initialize subscription has started. subscriptionDto: {}", subscriptionExternalDto);
        Subscription subscription = new Subscription();
        subscription.setPaymentsServiceSubscriptionId(subscriptionExternalDto.getPaymentsServiceSubscriptionId());
        subscription.setSubscriptionStatus(subscriptionExternalDto.getSubscriptionStatus());
        subscription.setAccountId(PassthroughHelper.retrievePassthroughValue(subscriptionExternalDto.getPassthroughDetails(), PassthroughType.ACCOUNT_ID));
        subscription.setWorkspaceId(PassthroughHelper.retrievePassthroughValue(subscriptionExternalDto.getPassthroughDetails(), PassthroughType.WORKSPACE_ID));
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
