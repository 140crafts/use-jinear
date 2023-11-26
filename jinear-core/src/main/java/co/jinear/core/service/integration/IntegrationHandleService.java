package co.jinear.core.service.integration;

import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.dto.integration.IntegrationScopeDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import co.jinear.core.model.vo.integration.InitializeIntegrationVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationHandleService {

    private final IntegrationInfoRetrieveService integrationInfoRetrieveService;
    private final IntegrationInfoOperationService integrationInfoOperationService;
    private final IntegrationScopeRetrieveService integrationScopeRetrieveService;
    private final IntegrationScopeOperationService integrationScopeOperationService;

    public String initializeOrUpdateInfo(String accountId, IntegrationProvider provider, IntegrationScopeType scope, String relatedObjectId) {
        Optional<IntegrationScopeDto> integrationScopeDtoOptional = integrationScopeRetrieveService.retrieveIntegrationWithScope(accountId, provider, scope);
        if (integrationScopeDtoOptional.isEmpty()) {
            return checkIntegrationInfoAndInitializeScope(accountId, provider, scope, relatedObjectId);
        }
        return integrationScopeDtoOptional.get().getIntegrationInfoId();
    }

    private String checkIntegrationInfoAndInitializeScope(String accountId, IntegrationProvider provider, IntegrationScopeType scope, String relatedObjectId) {
        IntegrationInfoDto integrationInfoDto = getIntegrationInfoDto(accountId, provider, relatedObjectId);
        integrationScopeOperationService.initializeScope(integrationInfoDto.getIntegrationInfoId(), scope);
        return integrationInfoDto.getIntegrationInfoId();
    }

    private IntegrationInfoDto initializeIntegrationInfo(String accountId, IntegrationProvider provider, String relatedObjectId) {
        InitializeIntegrationVo initializeIntegrationVo = new InitializeIntegrationVo(provider, accountId, relatedObjectId);
        return integrationInfoOperationService.initializeIntegration(initializeIntegrationVo);
    }

    private IntegrationInfoDto getIntegrationInfoDto(String accountId, IntegrationProvider provider, String relatedObjectId) {
        return integrationInfoRetrieveService
                .retrieveIntegrationWithProvider(accountId, provider)
                .orElseGet(() -> initializeIntegrationInfo(accountId, provider, relatedObjectId));
    }
}
