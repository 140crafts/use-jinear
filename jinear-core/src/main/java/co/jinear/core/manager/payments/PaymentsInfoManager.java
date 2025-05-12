package co.jinear.core.manager.payments;

import co.jinear.core.model.dto.payments.SubscriptionInfoDto;
import co.jinear.core.model.response.payments.RetrieveSubscriptionInfoResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.payments.PaymentsInfoRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentsInfoManager {

    private final PaymentsInfoRetrieveService paymentsInfoRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;

    public RetrieveSubscriptionInfoResponse retrieveSubscriptionInfo(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve payment info related with workspace has started. workspaceId: {}, currentAccountId: {}", workspaceId, currentAccountId);
        SubscriptionInfoDto subscriptionInfoDto = paymentsInfoRetrieveService.retrieveSubscriptionInfo(workspaceId);
        return mapResponse(subscriptionInfoDto);
    }

    private RetrieveSubscriptionInfoResponse mapResponse(SubscriptionInfoDto subscriptionInfoDto) {
        RetrieveSubscriptionInfoResponse retrieveSubscriptionInfoResponse = new RetrieveSubscriptionInfoResponse();
        retrieveSubscriptionInfoResponse.setSubscriptionInfoDto(subscriptionInfoDto);
        return retrieveSubscriptionInfoResponse;
    }
}
