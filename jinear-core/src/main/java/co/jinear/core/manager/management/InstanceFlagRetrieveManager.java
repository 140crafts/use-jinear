package co.jinear.core.manager.management;

import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.request.management.InstanceFlagOperationRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.management.InstanceFlagListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceFlagRetrieveManager {

    private final SessionInfoService sessionInfoService;
    private final InstanceFlagService instanceFlagService;

    public InstanceFlagListingResponse retrieveAll() {
        String accountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve all instance flags has started. accountId: {}", accountId);
        Map<InstanceFlagType, Object> instanceFlagTypeObjectMap = instanceFlagService.retrieveAll();
        return mapResponse(instanceFlagTypeObjectMap);
    }

    private InstanceFlagListingResponse mapResponse(Map<InstanceFlagType, Object> instanceFlagTypeObjectMap) {
        InstanceFlagListingResponse instanceFlagListingResponse = new InstanceFlagListingResponse();
        instanceFlagListingResponse.setInstanceFlagTypeObjectMap(instanceFlagTypeObjectMap);
        return instanceFlagListingResponse;
    }

    public BaseResponse set(@Valid InstanceFlagOperationRequest instanceFlagOperationRequest) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Set instance flag has started. accountId: {}, instanceFlagOperationRequest: {}", accountId, instanceFlagOperationRequest);
        instanceFlagService.set(instanceFlagOperationRequest.getInstanceFlagType(), instanceFlagOperationRequest.getValue());
        return new BaseResponse();
    }
}
