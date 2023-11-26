package co.jinear.core.service.integration.feed;

import co.jinear.core.model.dto.google.GmailThreadDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.integration.IntegrationFeedDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.service.google.GmailThreadOperationService;
import co.jinear.core.service.google.GmailThreadRetrieveService;
import co.jinear.core.service.google.GoogleTokenValidatedRetrieveService;
import co.jinear.core.service.integration.IntegrationInfoRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailIntegrationFeedRetrieveStrategy implements IntegrationFeedRetrieveStrategy<GmailThreadDto> {

    private static final Long REFETCH_THRESHOLD = 1L;
    private final IntegrationInfoRetrieveService integrationInfoRetrieveService;
    private final GoogleTokenValidatedRetrieveService googleTokenValidatedRetrieveService;
    private final GmailThreadOperationService gmailThreadOperationService;
    private final GmailThreadRetrieveService gmailThreadRetrieveService;

    @Override
    public IntegrationProvider getProvider() {
        return IntegrationProvider.GOOGLE;
    }

    @Override
    public IntegrationFeedDto<GmailThreadDto> retrieveFeed(String integrationId, int page) {
        IntegrationInfoDto integrationInfoDto = integrationInfoRetrieveService.retrieve(integrationId);
        String googleUserInfoId = integrationInfoDto.getRelatedObjectId();
        GoogleTokenDto googleTokenDto = googleTokenValidatedRetrieveService.retrieveValidatedToken(googleUserInfoId);
        checkAndFetchNewData(googleTokenDto);
        Page<GmailThreadDto> gmailThreadDtos = gmailThreadRetrieveService.listThreads(googleTokenDto.getGoogleTokenId(), page);
        return mapResult(gmailThreadDtos);
    }

    private void checkAndFetchNewData(GoogleTokenDto googleTokenDto) {
        ZonedDateTime lastMailCheck = googleTokenDto.getLastMailCheck();
        ZonedDateTime thresholdDate = ZonedDateTime.now().minusMinutes(REFETCH_THRESHOLD);
        log.info("Checking fetch needed. lastMailCheck: {}, thresholdDate: {}", lastMailCheck, thresholdDate);
        if (Objects.isNull(lastMailCheck) || thresholdDate.isAfter(lastMailCheck)) {
            gmailThreadOperationService.fetchAndSaveLatestThreads(googleTokenDto);
        }
    }

    private IntegrationFeedDto<GmailThreadDto> mapResult(Page<GmailThreadDto> gmailThreadDtos) {
        IntegrationFeedDto<GmailThreadDto> integrationFeedDto = new IntegrationFeedDto<>();
        integrationFeedDto.setData(gmailThreadDtos);
        return integrationFeedDto;
    }
}
