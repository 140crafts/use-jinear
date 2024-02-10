package co.jinear.core.service.integration.calendar;

import co.jinear.core.converter.google.calendar.GoogleCalendarEventInfoToCalendarEventDtoConverter;
import co.jinear.core.converter.google.calendar.GoogleCalendarInfoToExternalCalendarSourceDtoConverter;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.vo.calendar.UpdateExternalEventDatesVo;
import co.jinear.core.service.google.GoogleTokenValidatedRetrieveService;
import co.jinear.core.service.google.calendar.GoogleCalendarInMemoryCacheService;
import co.jinear.core.system.gcloud.googleapis.GoogleApisClient;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventDate;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import co.jinear.core.system.util.ZonedDateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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
    private final GoogleCalendarEventInfoToCalendarEventDtoConverter googleCalendarEventInfoToCalendarEventDtoConverter;

    @Override
    public IntegrationProvider getProvider() {
        return IntegrationProvider.GOOGLE;
    }

    @Override
    public ExternalCalendarSourceDto retrieveCalendarSource(IntegrationInfoDto integrationInfoDto, String externalCalendarSourceId) {
        return Optional.of(externalCalendarSourceId)
                .filter(googleCalendarInMemoryCacheService::hasCachedCalendarSource)
                .map(googleCalendarInMemoryCacheService::getCachedCalendarSource)
                .orElseGet(() -> retrieveAndCacheCalendarSource(integrationInfoDto, externalCalendarSourceId));
    }

    @Override
    public List<ExternalCalendarSourceDto> retrieveCalendarSources(IntegrationInfoDto integrationInfoDto) {
        try {
            //TODO Handle Google 401 errors
            return Optional.of(integrationInfoDto)
                    .map(IntegrationInfoDto::getGoogleUserInfo)
                    .map(GoogleUserInfoDto::getGoogleUserInfoId)
                    .filter(googleCalendarInMemoryCacheService::hasCachedCalendarSourceList)
                    .map(googleCalendarInMemoryCacheService::getCachedCalendarSourceList)
                    .orElseGet(() -> retrieveAndCacheSources(integrationInfoDto));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CalendarEventDto> retrieveCalendarEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest) {
        return Optional.of(retrieveEventListRequest)
                .filter(googleCalendarInMemoryCacheService::hasCachedCalendarEventList)
                .map(googleCalendarInMemoryCacheService::getCachedCalendarEventList)
                .orElseGet(() -> retrieveAndCacheEvents(integrationInfoDto, retrieveEventListRequest));
    }

    @Override
    public CalendarEventDto retrieveCalendarEvent(IntegrationInfoDto integrationInfoDto, String calendarSourceId, String eventId) {
        ExternalCalendarSourceDto externalCalendarSourceDto = retrieveCalendarSource(integrationInfoDto, calendarSourceId);
        return Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .map(googleTokenValidatedRetrieveService::retrieveValidatedToken)
                .map(GoogleTokenDto::getAccessToken)
                .map(token -> googleApisClient.retrieveEvent(token, calendarSourceId, eventId))
                .map(googleCalendarEventInfo -> googleCalendarEventInfoToCalendarEventDtoConverter.mapCalendarEventToCalendarEventDto(externalCalendarSourceDto, googleCalendarEventInfo))
                .orElse(null);
    }

    @Override
    public void updateCalendarEventDates(IntegrationInfoDto integrationInfoDto, UpdateExternalEventDatesVo updateExternalEventDatesVo) {
        log.info("Update calendar event dates has started. updateExternalEventDatesVo: {}", updateExternalEventDatesVo);
        String accessToken = Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .map(googleTokenValidatedRetrieveService::retrieveValidatedToken)
                .map(GoogleTokenDto::getAccessToken)
                .orElseThrow();

        GoogleCalendarEventInfo googleCalendarEventInfo = googleApisClient.retrieveEvent(accessToken, updateExternalEventDatesVo.getCalendarSourceId(), updateExternalEventDatesVo.getCalendarEventId());

        GoogleCalendarEventDate currentStart = googleCalendarEventInfo.getStart();
        ZonedDateTime nextStartDateTime = ZonedDateHelper.atTimeZone(updateExternalEventDatesVo.getAssignedDate(), currentStart.getTimeZone());
        GoogleCalendarEventDate nextStart = new GoogleCalendarEventDate();
        nextStart.setTimeZone(currentStart.getTimeZone());
        if (Boolean.TRUE.equals(updateExternalEventDatesVo.getHasPreciseAssignedDate())) {
            nextStart.setDateTime(ZonedDateHelper.formatWithDateTimeFormat5(nextStartDateTime));
        } else {
            nextStart.setDate(ZonedDateHelper.formatWithDateTimeFormat4(nextStartDateTime));
        }
        googleCalendarEventInfo.setStart(nextStart);

        GoogleCalendarEventDate currentEnd = googleCalendarEventInfo.getEnd();
        ZonedDateTime nextEndDateTime = ZonedDateHelper.atTimeZone(updateExternalEventDatesVo.getDueDate(), currentEnd.getTimeZone());
        GoogleCalendarEventDate nextEnd = new GoogleCalendarEventDate();
        nextEnd.setTimeZone(currentEnd.getTimeZone());
        if (Boolean.TRUE.equals(updateExternalEventDatesVo.getHasPreciseDueDate())) {
            nextEnd.setDateTime(ZonedDateHelper.formatWithDateTimeFormat5(nextEndDateTime));
        } else {
            nextEnd.setDate(ZonedDateHelper.formatWithDateTimeFormat4(nextStartDateTime));
        }
        googleCalendarEventInfo.setEnd(nextEnd);

        googleApisClient.updateEvent(accessToken, updateExternalEventDatesVo.getCalendarSourceId(), googleCalendarEventInfo);
        googleCalendarInMemoryCacheService.clearCalendarEventListCache(updateExternalEventDatesVo.getCalendarSourceId());
    }

    private ExternalCalendarSourceDto retrieveAndCacheCalendarSource(IntegrationInfoDto integrationInfoDto, String externalCalendarSourceId) {
        log.info("Retrieve calendar source externalCalendarSourceId: {}", externalCalendarSourceId);
        return Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .map(googleTokenValidatedRetrieveService::retrieveValidatedToken)
                .map(GoogleTokenDto::getAccessToken)
                .map(token -> googleApisClient.retrieveCalendar(token, externalCalendarSourceId))
                .map(googleCalendarInfoToExternalCalendarSourceDtoConverter::convert)
                .map(externalCalendarSourceDto -> googleCalendarInMemoryCacheService.cacheCalendarSource(externalCalendarSourceId, externalCalendarSourceDto))
                .orElse(null);
    }

    private List<CalendarEventDto> retrieveAndCacheEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest) {
        return Optional.of(integrationInfoDto)
                .map(IntegrationInfoDto::getGoogleUserInfo)
                .map(GoogleUserInfoDto::getGoogleUserInfoId)
                .map(googleTokenValidatedRetrieveService::retrieveValidatedToken)
                .map(GoogleTokenDto::getAccessToken)
                .map(token -> googleApisClient.retrieveEventList(token, retrieveEventListRequest))
                .map(googleCalendarEventListResponse -> {
                    String calendarSourceId = retrieveEventListRequest.getCalendarSourceId();
                    List<GoogleCalendarEventInfo> items = googleCalendarEventListResponse.getItems();
                    return items.stream()
                            .map(googleCalendarEventInfo -> googleCalendarEventInfoToCalendarEventDtoConverter.mapCalendarEventToCalendarEventDto(calendarSourceId, googleCalendarEventListResponse, googleCalendarEventInfo))
                            .toList();
                })
                .map(calendarEventDtoList -> googleCalendarInMemoryCacheService.cacheCalendarEventList(retrieveEventListRequest, calendarEventDtoList))
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
