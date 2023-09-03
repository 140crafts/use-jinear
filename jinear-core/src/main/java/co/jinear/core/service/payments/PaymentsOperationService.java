package co.jinear.core.service.payments;

import co.jinear.core.converter.payments.ClientSubscriptionDtoToSubscriptionExternalDtoConverter;
import co.jinear.core.model.dto.payments.SubscriptionExternalDto;
import co.jinear.core.model.entity.payments.Subscription;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.service.client.paymentprocessor.PaymentProcessorClient;
import co.jinear.core.service.client.paymentprocessor.model.dto.purchase.PurchaseListingDto;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.ProductType;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import co.jinear.core.service.workspace.WorkspaceTierService;
import co.jinear.core.system.util.payment.PassthroughHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static co.jinear.core.service.client.paymentprocessor.model.enumtype.PassthroughType.WORKSPACE_ID;
import static co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentsOperationService {

    private static final List<SubscriptionStatus> ACTIVE_PLAN_STATUSES = List.of(ACTIVE, TRIALING, PAST_DUE);
    private static final List<SubscriptionStatus> PASSIVE_PLAN_STATUSES = List.of(PAUSED, DELETED);

    private final PaymentProcessorClient paymentProcessorClient;
    private final SubscriptionRetrieveService subscriptionRetrieveService;
    private final ClientSubscriptionDtoToSubscriptionExternalDtoConverter clientSubscriptionDtoToSubscriptionExternalDtoConverter;
    private final SubscriptionOperationService subscriptionOperationService;
    private final WorkspaceTierService workspaceTierService;

    public void retrieveAndApplyLatestPayments(ZonedDateTime lastSyncDate) {
        log.info("Retrieve and apply latest payments has started.");
        PurchaseListingDto purchaseListingDto = retrievePurchasesAfter(lastSyncDate);
        purchaseListingDto.getSubscriptionDtoList().stream()
                .map(clientSubscriptionDtoToSubscriptionExternalDtoConverter::convert)
                .forEach(this::initializeOrUpdateSubscription);
    }

    private PurchaseListingDto retrievePurchasesAfter(ZonedDateTime lastSyncDate) {
        log.info("Retrieving purchase updates. lastSyncDate: {}", lastSyncDate);
        String formatted = lastSyncDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return paymentProcessorClient.retrievePurchasesAfter(ProductType.JINEAR, formatted).getPurchaseListingDto();
    }

    private void initializeOrUpdateSubscription(SubscriptionExternalDto subscriptionExternalDto) {
        log.info("Initialize or update subscription has started. subscriptionDto: {}", subscriptionExternalDto);
        try {
            subscriptionRetrieveService.retrieveSubscriptionWithPaymentServiceId(subscriptionExternalDto.getPaymentsServiceSubscriptionId())
                    .ifPresentOrElse(
                            subscription -> updateExistingSubscription(subscription, subscriptionExternalDto),
                            () -> initializeSubscription(subscriptionExternalDto));
        } catch (Exception e) {
            log.error("Initialize or update subscription has failed. subscriptionDto: {}", subscriptionExternalDto);
        }
    }

    @Transactional
    public void updateExistingSubscription(Subscription subscription, SubscriptionExternalDto subscriptionExternalDto) {
        log.info("Update existing subscription has started. subscriptionId: {}, subscriptionDto: {}", subscription.getSubscriptionId(), subscriptionExternalDto);
        updatePlan(subscription, subscriptionExternalDto);
        subscriptionOperationService.updateSubscriptionStatus(subscription, subscriptionExternalDto.getSubscriptionStatus());
    }

    @Transactional
    public void initializeSubscription(SubscriptionExternalDto subscriptionExternalDto) {
        log.info("Initialize subscription has started. subscriptionDto: {}", subscriptionExternalDto);
        initializePlan(subscriptionExternalDto);
        subscriptionOperationService.initializeSubscription(subscriptionExternalDto);
    }

    private void updatePlan(Subscription subscription, SubscriptionExternalDto subscriptionExternalDto) {
        SubscriptionStatus oldSubscriptionStatus = subscription.getSubscriptionStatus();
        SubscriptionStatus newSubscriptionStatus = subscriptionExternalDto.getSubscriptionStatus();
        String workspaceId = PassthroughHelper.retrievePassthroughValue(subscriptionExternalDto.getPassthroughDetails(), WORKSPACE_ID);
        log.info("Change plan has started. oldSubscriptionStatus: {}, newSubscriptionStatus: {}", oldSubscriptionStatus, newSubscriptionStatus);
        if (ACTIVE_PLAN_STATUSES.contains(oldSubscriptionStatus) && PASSIVE_PLAN_STATUSES.contains(newSubscriptionStatus)) {
            workspaceTierService.updateWorkspaceTier(workspaceId, WorkspaceTier.BASIC);
        } else if (PASSIVE_PLAN_STATUSES.contains(oldSubscriptionStatus) && ACTIVE_PLAN_STATUSES.contains(newSubscriptionStatus)) {
            workspaceTierService.updateWorkspaceTier(workspaceId, WorkspaceTier.PRO);
        }
    }

    private void initializePlan(SubscriptionExternalDto subscriptionExternalDto) {
        SubscriptionStatus newSubscriptionStatus = subscriptionExternalDto.getSubscriptionStatus();
        log.info("Initialize plan has started. newSubscriptionStatus: {}", newSubscriptionStatus);
        if (ACTIVE_PLAN_STATUSES.contains(newSubscriptionStatus)) {
            String workspaceId = PassthroughHelper.retrievePassthroughValue(subscriptionExternalDto.getPassthroughDetails(), WORKSPACE_ID);
            workspaceTierService.updateWorkspaceTier(workspaceId, WorkspaceTier.PRO);
        }
    }
}
