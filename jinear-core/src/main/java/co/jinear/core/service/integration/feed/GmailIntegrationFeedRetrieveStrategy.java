package co.jinear.core.service.integration.feed;

import co.jinear.core.converter.integration.GmailThreadVoToFeedItemDtoConverter;
import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.dto.integration.FeedContentDto;
import co.jinear.core.model.dto.integration.FeedContentItemDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.service.google.GmailApiCallerService;
import co.jinear.core.service.google.GmailInMemoryCacheService;
import co.jinear.core.service.google.GoogleTokenValidatedRetrieveService;
import co.jinear.core.service.integration.IntegrationInfoRetrieveService;
import co.jinear.core.system.gcloud.gmail.model.GmailThreadInfo;
import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class GmailIntegrationFeedRetrieveStrategy implements IntegrationFeedRetrieveStrategy {

    private final IntegrationInfoRetrieveService integrationInfoRetrieveService;
    private final GoogleTokenValidatedRetrieveService googleTokenValidatedRetrieveService;
    private final GmailApiCallerService gmailApiCallerService;
    private final GmailInMemoryCacheService gmailInMemoryCacheService;
    private final GmailThreadVoToFeedItemDtoConverter gmailThreadVoToFeedItemDtoConverter;

    @Override
    public IntegrationProvider getProvider() {
        return IntegrationProvider.GOOGLE;
    }

    @Override
    public FeedContentDto retrieveFeed(FeedDto feedDto, String pageToken) {
        String integrationInfoId = feedDto.getIntegrationInfoId();
        log.info("Retrieve feed has started for integrationInfoId: {} with pageToken: {}", integrationInfoId, pageToken);
        IntegrationInfoDto integrationInfoDto = integrationInfoRetrieveService.retrieve(integrationInfoId);
        String googleUserInfoId = integrationInfoDto.getRelatedObjectId();
        GoogleTokenDto googleTokenDto = googleTokenValidatedRetrieveService.retrieveValidatedToken(googleUserInfoId);
        GoogleUserInfoDto googleUserInfoDto = googleTokenDto.getGoogleUserInfo();
        String googleUserId = googleUserInfoDto.getSub();
        String googleUserEmail = googleUserInfoDto.getEmail();

        GmailListThreadsResponse gmailListThreadsResponse = listThreads(pageToken, googleTokenDto, googleUserId);
        List<RetrieveBatchRequestVo> messageVoList = mapBatchRequest(googleUserId, gmailListThreadsResponse);
        List<GmailThreadVo> gmailThreadVos = getBatchThreads(googleTokenDto, messageVoList);

        return mapResultFeed(googleUserEmail, feedDto, gmailListThreadsResponse, gmailThreadVos);
    }

    public FeedContentItemDto retrieveFeedItem(FeedDto feedDto, String threadId) {
        String integrationInfoId = feedDto.getIntegrationInfoId();
        log.info("Retrieve feed item has started for integrationInfoId: {}", integrationInfoId);
        IntegrationInfoDto integrationInfoDto = integrationInfoRetrieveService.retrieve(integrationInfoId);
        String googleUserInfoId = integrationInfoDto.getRelatedObjectId();
        GoogleTokenDto googleTokenDto = googleTokenValidatedRetrieveService.retrieveValidatedToken(googleUserInfoId);
        String googleUserId = googleTokenDto.getGoogleUserInfo().getSub();

        GmailThreadVo gmailThreadVo = getThread(threadId, googleUserId, googleTokenDto);
        return gmailThreadVoToFeedItemDtoConverter.convert(feedDto, gmailThreadVo, false);
    }

    private GmailThreadVo getThread(String threadId, String googleUserId, GoogleTokenDto googleTokenDto) {
        if (gmailInMemoryCacheService.hasCachedThreadDetailResponse(threadId, googleUserId)) {
            return gmailInMemoryCacheService.getCachedThreadDetailResponse(threadId, googleUserId);
        }
        GmailThreadVo gmailThreadVo = gmailApiCallerService.getThread(googleUserId, threadId, googleTokenDto.getAccessToken());
        gmailInMemoryCacheService.cacheThreadDetailResponse(threadId, googleUserId, gmailThreadVo);
        return gmailThreadVo;
    }

    private GmailListThreadsResponse listThreads(String pageToken, GoogleTokenDto googleTokenDto, String googleUserId) {
        if (gmailInMemoryCacheService.hasCachedThreadsResponse(googleUserId, pageToken)) {
            log.info("Returning list threads response from cache.");
            return gmailInMemoryCacheService.getCachedThreadsResponse(googleUserId, pageToken);
        }
        GmailListThreadsResponse gmailListThreadsResponse = gmailApiCallerService.listThreads(googleUserId, googleTokenDto.getAccessToken(), pageToken);
        gmailInMemoryCacheService.cacheThreadsResponse(googleUserId, pageToken, gmailListThreadsResponse);
        return gmailListThreadsResponse;
    }

    private List<GmailThreadVo> getBatchThreads(GoogleTokenDto googleTokenDto, List<RetrieveBatchRequestVo> messageVoList) {
        if (gmailInMemoryCacheService.hasCachedThreadDetailListResponse(messageVoList)) {
            log.info("Returning batch threads response from cache.");
            return gmailInMemoryCacheService.getCachedThreadDetailListResponse(messageVoList);
        }
        List<GmailThreadVo> gmailThreadVos = gmailApiCallerService.getBatchThreads(googleTokenDto.getAccessToken(), messageVoList);
        gmailInMemoryCacheService.cacheThreadDetailListResponse(messageVoList, gmailThreadVos);
        gmailThreadVos.forEach(gmailThreadVo -> gmailInMemoryCacheService.cacheThreadDetailResponse(gmailThreadVo.getId(), googleTokenDto.getGoogleUserInfo().getSub(), gmailThreadVo));
        return gmailThreadVos;
    }

    private List<RetrieveBatchRequestVo> mapBatchRequest(String googleUserId, GmailListThreadsResponse gmailListThreadsResponse) {
        return Optional.of(gmailListThreadsResponse)
                .map(GmailListThreadsResponse::getThreads)
                .map(gmailThreadInfos -> gmailThreadInfos.stream()
                        .map(gmailThreadInfo -> new RetrieveBatchRequestVo(googleUserId, gmailThreadInfo.getId()))
                        .toList()
                )
                .orElse(Collections.emptyList());
    }

    private FeedContentDto mapResultFeed(String googleUserEmail, FeedDto feedDto, GmailListThreadsResponse gmailListThreadsResponse, List<GmailThreadVo> gmailThreadVos) {
        List<GmailThreadInfo> threads = gmailListThreadsResponse.getThreads();
        Map<String, GmailThreadInfo> threadMap = threads.stream().collect(Collectors.toMap(GmailThreadInfo::getId, Function.identity()));

        List<FeedContentItemDto> detailedFeedContentItemDtos = gmailThreadVos.stream()
                .map(gmailThreadVo -> {
                    GmailThreadInfo gmailThreadInfo = threadMap.get(gmailThreadVo.getId());
                    return gmailThreadVoToFeedItemDtoConverter.convert(googleUserEmail, gmailThreadInfo, feedDto, gmailThreadVo, true);
                })
                .toList();

        FeedContentDto feedContentDto = new FeedContentDto();
        feedContentDto.setFeedItemList(detailedFeedContentItemDtos);
        feedContentDto.setNextPageToken(gmailListThreadsResponse.getNextPageToken());
        feedContentDto.setFeed(feedDto);

        return feedContentDto;
    }
}
