package co.jinear.core.service.integration;

import co.jinear.core.converter.integration.IntegrationInfoDtoConverter;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.vo.integration.InitializeIntegrationVo;
import co.jinear.core.repository.IntegrationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationInfoOperationService {

    private final IntegrationInfoRepository integrationInfoRepository;
    private final IntegrationInfoDtoConverter integrationInfoDtoConverter;

    public IntegrationInfoDto initializeIntegration(InitializeIntegrationVo initializeIntegrationVo) {
        log.info("Initialize integration has started. initializeIntegrationVo: {}", initializeIntegrationVo);
        IntegrationInfo integrationInfo = mapToEntity(initializeIntegrationVo);
        IntegrationInfo saved = integrationInfoRepository.save(integrationInfo);
        return integrationInfoDtoConverter.map(saved);
    }

    private IntegrationInfo mapToEntity(InitializeIntegrationVo initializeIntegrationVo) {
        IntegrationInfo integrationInfo = new IntegrationInfo();
        integrationInfo.setProvider(initializeIntegrationVo.getProvider());
        integrationInfo.setAccountId(initializeIntegrationVo.getAccountId());
        integrationInfo.setRelatedObjectId(initializeIntegrationVo.getRelatedObjectId());
        return integrationInfo;
    }
}
