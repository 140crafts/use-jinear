package co.jinear.core.service.payments;

import co.jinear.core.converter.payments.SubscriptionEntityToSubscriptionDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.payments.SubscriptionDto;
import co.jinear.core.model.entity.payments.Subscription;
import co.jinear.core.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionRetrieveService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionEntityToSubscriptionDtoConverter subscriptionEntityToSubscriptionDtoConverter;

    public Optional<Subscription> retrieveSubscriptionWithPaymentServiceId(String paymentServiceId) {
        log.info("Retrieve subscription with payment service id has started. paymentServiceId: {}", paymentServiceId);
        return subscriptionRepository.findByPaymentsServiceSubscriptionIdAndPassiveIdIsNull(paymentServiceId);
    }

    public SubscriptionDto retrieveSubscriptionWithWorkspaceId(String workspaceId) {
        log.info("Retrieve subscription with workspace id has started. workspaceId: {}", workspaceId);
        return subscriptionRepository.findByWorkspaceIdAndPassiveIdIsNull(workspaceId)
                .map(subscriptionEntityToSubscriptionDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }
}
