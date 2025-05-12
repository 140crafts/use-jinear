package co.jinear.core.service.payments;

import co.jinear.core.converter.payments.ExternalSubscriptionInfoDtoToSubscriptionInfoDtoConverter;
import co.jinear.core.model.dto.payments.SubscriptionDto;
import co.jinear.core.model.dto.payments.SubscriptionInfoDto;
import co.jinear.core.service.client.paymentprocessor.PaymentProcessorClient;
import co.jinear.core.service.client.paymentprocessor.model.dto.ExternalSubscriptionInfoDto;
import co.jinear.core.service.client.paymentprocessor.model.response.RetrieveSubscriptionInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentsInfoRetrieveService {

    private final SubscriptionRetrieveService subscriptionRetrieveService;
    private final PaymentProcessorClient paymentProcessorClient;
    private final ExternalSubscriptionInfoDtoToSubscriptionInfoDtoConverter externalSubscriptionInfoDtoToSubscriptionInfoDtoConverter;

    public SubscriptionInfoDto retrieveSubscriptionInfo(String workspaceId) {
        log.info("Retrieve subscription info has started. workspaceId: {}", workspaceId);
        SubscriptionDto subscriptionDto = subscriptionRetrieveService.retrieveSubscriptionWithWorkspaceId(workspaceId);
        RetrieveSubscriptionInfoResponse retrieveSubscriptionInfoResponse = paymentProcessorClient.retrieveSubscriptionInfo(subscriptionDto.getPaymentsServiceSubscriptionId());
        ExternalSubscriptionInfoDto externalSubscriptionInfoDto = retrieveSubscriptionInfoResponse.getSubscriptionInfo();
        return externalSubscriptionInfoDtoToSubscriptionInfoDtoConverter.convert(externalSubscriptionInfoDto);
    }
}
