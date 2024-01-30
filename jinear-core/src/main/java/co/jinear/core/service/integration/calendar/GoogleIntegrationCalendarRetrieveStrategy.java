package co.jinear.core.service.integration.calendar;

import co.jinear.core.converter.google.calendar.GoogleCalendarEventInfoToTaskDtoConverter;
import co.jinear.core.converter.google.calendar.GoogleCalendarInfoToExternalCalendarSourceDtoConverter;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.service.google.GoogleTokenValidatedRetrieveService;
import co.jinear.core.service.google.calendar.GoogleCalendarInMemoryCacheService;
import co.jinear.core.system.gcloud.googleapis.GoogleApisClient;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleIntegrationCalendarRetrieveStrategy implements IntegrationCalendarRetrieveStrategy {

    private final GoogleTokenValidatedRetrieveService googleTokenValidatedRetrieveService;
    private final GoogleApisClient googleApisClient;
    private final GoogleCalendarInMemoryCacheService googleCalendarInMemoryCacheService;
    private final GoogleCalendarInfoToExternalCalendarSourceDtoConverter googleCalendarInfoToExternalCalendarSourceDtoConverter;
    private final GoogleCalendarEventInfoToTaskDtoConverter googleCalendarEventInfoToTaskDtoConverter;

    @Override
    public IntegrationProvider getProvider() {
        return IntegrationProvider.GOOGLE;
    }

    @Override
    public List<ExternalCalendarSourceDto> retrieveCalendarSources(IntegrationInfoDto integrationInfoDto) {
        return Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .filter(googleCalendarInMemoryCacheService::hasCachedCalendarSourceList)
                .map(googleCalendarInMemoryCacheService::getCachedCalendarSourceList)
                .orElseGet(() -> retrieveAndCacheSources(integrationInfoDto));
    }

    @Override
    public List<TaskDto> retrieveCalendarEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest) {
        return Optional.of(retrieveEventListRequest)
                .filter(googleCalendarInMemoryCacheService::hasCachedCalendarEventList)
                .map(googleCalendarInMemoryCacheService::getCachedCalendarEventList)
                .orElseGet(() -> retrieveAndCacheEvents(integrationInfoDto, retrieveEventListRequest));
    }

    private List<TaskDto> retrieveAndCacheEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest) {
        return Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .map(googleTokenValidatedRetrieveService::retrieveValidatedToken)
                .map(GoogleTokenDto::getAccessToken)
                .map(token -> googleApisClient.retrieveEventList(token, retrieveEventListRequest))
                .map(googleCalendarEventListResponse -> {
                    String calendarId = retrieveEventListRequest.getCalendarId();
                    List<GoogleCalendarEventInfo> items = googleCalendarEventListResponse.getItems();
                    return items.stream()
                            .map(googleCalendarEventInfo -> googleCalendarEventInfoToTaskDtoConverter.mapCalendarEventToTaskDto(calendarId, googleCalendarEventListResponse, googleCalendarEventInfo))
                            .toList();
                })
                .map(taskDtoList -> googleCalendarInMemoryCacheService.cacheCalendarEventList(retrieveEventListRequest, taskDtoList))
                .orElseGet(Collections::emptyList);
    }

    private List<ExternalCalendarSourceDto> retrieveAndCacheSources(IntegrationInfoDto integrationInfoDto) {
        return Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .map(googleTokenValidatedRetrieveService::retrieveValidatedToken)
                .map(GoogleTokenDto::getAccessToken)
                .map(googleApisClient::retrieveCalendarList)
                .map(GoogleCalendarListResponse::getItems)
                .map(Collection::stream)
                .map(this::mapToExternalCalendarSourceList)
                .map(externalCalendarSourceDtos -> googleCalendarInMemoryCacheService.cacheCalendarSourceList(integrationInfoDto.getGoogleUserInfo().getGoogleUserInfoId(), externalCalendarSourceDtos))
                .orElseGet(Collections::emptyList);
    }

    private List<ExternalCalendarSourceDto> mapToExternalCalendarSourceList(Stream<GoogleCalendarInfo> googleCalendarInfoStream) {
        return googleCalendarInfoStream
                .map(googleCalendarInfoToExternalCalendarSourceDtoConverter::convert)
                .toList();
    }
}
