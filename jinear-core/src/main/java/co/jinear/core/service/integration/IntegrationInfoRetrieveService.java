package co.jinear.core.service.integration;

import co.jinear.core.converter.integration.IntegrationInfoDtoConverter;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.repository.IntegrationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationInfoRetrieveService {

    private final IntegrationInfoRepository integrationInfoRepository;
    private final IntegrationInfoDtoConverter integrationInfoDtoConverter;

    public Optional<IntegrationInfoDto> retrieveIntegrationWithProvider(String accountId, IntegrationProvider provider) {
        log.info("Retrieve integration with provider has started. accountId: {}, provider: {}", accountId, provider);
        return integrationInfoRepository.findByAccountIdAndProviderAndPassiveIdIsNull(accountId, provider)
                .map(integrationInfoDtoConverter::map);
    }
}
