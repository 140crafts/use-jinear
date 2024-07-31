package co.jinear.core.service.calendar;

import co.jinear.core.converter.calendar.RetrieveEventListRequestConverter;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.calendar.CalendarMemberDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.vo.calendar.CalendarEventSearchFilterVo;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategy;
import co.jinear.core.service.integration.calendar.IntegrationCalendarRetrieveStrategyFactory;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
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
public class CalendarExternalEventRetrieveService {

    private final CalendarMemberRetrieveService calendarMemberRetrieveService;
    private final RetrieveEventListRequestConverter retrieveEventListRequestConverter;
    private final IntegrationCalendarRetrieveStrategyFactory integrationCalendarRetrieveStrategyFactory;

    public List<CalendarEventDto> retrieveExternalCalendarTasks(String currentAccount, CalendarEventSearchFilterVo calendarEventSearchFilterVo) {
        log.info("Retrieve calendar tasks has started. currentAccount: {}, calendarEventSearchFilterVo: {}", currentAccount, calendarEventSearchFilterVo);
        List<String> requestedCalendarIds = mapRequestedCalendarIds(calendarEventSearchFilterVo);

        List<CalendarMemberDto> accountCalendarMemberships = Optional.of(requestedCalendarIds)
                .filter(ids -> !ids.isEmpty())
                .map(ids -> calendarMemberRetrieveService.retrieveCalendarsIncludingExternalSources(currentAccount, calendarEventSearchFilterVo.getWorkspaceId(), ids))
                .orElseGet(() -> calendarMemberRetrieveService.retrieveAccountCalendarsIncludingExternalSources(currentAccount, calendarEventSearchFilterVo.getWorkspaceId()));

        return accountCalendarMemberships
                .stream()
                .map(CalendarMemberDto::getCalendar)
                .map(calendarDto -> convertAndRetrieveEventsFromCalendar(calendarEventSearchFilterVo, calendarDto))
                .flatMap(List::stream)
                .toList();
    }

    private List<CalendarEventDto> convertAndRetrieveEventsFromCalendar(CalendarEventSearchFilterVo calendarEventSearchFilterVo, CalendarDto calendarDto) {
        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        return Optional.of(calendarDto)
                .map(CalendarDto::getCalendarSources)
                .map(Collection::stream)
                .map(sourceStream -> sourceStream
                        .map(sourceDto -> retrieveEventListRequestConverter.convert(calendarEventSearchFilterVo, sourceDto))
                        .map(request -> retrieveExternalSourceEvents(integrationInfoDto, request))
                        .flatMap(List::stream)
                        .map(calendarEventDto -> mapCalendarAndWorkspaceId(calendarEventDto, calendarDto, calendarEventSearchFilterVo))
                )
                .map(Stream::toList)
                .orElseGet(Collections::emptyList);
    }

    private CalendarEventDto mapCalendarAndWorkspaceId(CalendarEventDto calendarEventDto, CalendarDto calendarDto, CalendarEventSearchFilterVo calendarEventSearchFilterVo) {
        calendarEventDto.setCalendarId(calendarDto.getCalendarId());
        calendarEventDto.setWorkspaceId(calendarEventSearchFilterVo.getWorkspaceId());
        return calendarEventDto;
    }

    private List<String> mapRequestedCalendarIds(CalendarEventSearchFilterVo calendarEventSearchFilterVo) {
        log.info("Map requested calendar ids has started.");
        return Optional.of(calendarEventSearchFilterVo)
                .map(CalendarEventSearchFilterVo::getCalendarIdList)
                .orElseGet(Collections::emptyList);
    }

    private List<CalendarEventDto> retrieveExternalSourceEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest) {
        log.info("Retrieve external source events has started. retrieveEventListRequest: {}", retrieveEventListRequest);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        return integrationCalendarRetrieveStrategy.retrieveCalendarEvents(integrationInfoDto, retrieveEventListRequest);
    }
}
