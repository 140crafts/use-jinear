package co.jinear.core.service.calendar;

import co.jinear.core.converter.calendar.RetrieveEventListRequestConverter;
import co.jinear.core.model.dto.calendar.*;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
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
    private final CalendarExternalSourceRetrieveService calendarExternalSourceRetrieveService;
    private final IntegrationCalendarRetrieveStrategyFactory integrationCalendarRetrieveStrategyFactory;

    public List<CalendarEventDto> retrieveExternalCalendarTasks(String currentAccount, TaskSearchFilterVo taskSearchFilterVo) {
        log.info("Retrieve calendar tasks has started. currentAccount: {}, taskSearchFilterVo: {}", currentAccount, taskSearchFilterVo);
        List<String> requestedCalendarIds = mapRequestedCalendarIds(taskSearchFilterVo);

        List<CalendarMemberDto> accountCalendarMemberships = Optional.of(requestedCalendarIds)
                .filter(ids -> !ids.isEmpty())
                .map(ids -> calendarMemberRetrieveService.retrieveCalendarsIncludingExternalSources(currentAccount, taskSearchFilterVo.getWorkspaceId(), ids))
                .orElseGet(() -> calendarMemberRetrieveService.retrieveAccountCalendarsIncludingExternalSources(currentAccount, taskSearchFilterVo.getWorkspaceId()));

        return accountCalendarMemberships
                .stream()
                .map(CalendarMemberDto::getCalendar)
                .map(calendarDto -> convertAndRetrieveEveryCalendars(taskSearchFilterVo, calendarDto))
                .flatMap(List::stream)
                .toList();
    }

    private List<CalendarEventDto> convertAndRetrieveEveryCalendars(TaskSearchFilterVo taskSearchFilterVo, CalendarDto calendarDto) {
        IntegrationInfoDto integrationInfoDto = calendarDto.getIntegrationInfo();
        List<TaskExternalCalendarFilterDto> externalCalendarList = getExternalCalendarFilterIfEmptyRetrieveAllSources(taskSearchFilterVo, calendarDto, integrationInfoDto);
        return externalCalendarList.stream()
                .map(taskExternalCalendarFilterDto -> retrieveEventListRequestConverter.convert(taskSearchFilterVo, taskExternalCalendarFilterDto))
                .map(retrieveEventListRequest -> retrieveExternalSourceEvents(integrationInfoDto, retrieveEventListRequest))
                .flatMap(List::stream)
                .toList();
    }

    private List<TaskExternalCalendarFilterDto> getExternalCalendarFilterIfEmptyRetrieveAllSources(TaskSearchFilterVo taskSearchFilterVo, CalendarDto calendarDto, IntegrationInfoDto integrationInfoDto) {
        return Optional.of(taskSearchFilterVo)
                .map(TaskSearchFilterVo::getExternalCalendarList)
                .filter(externalCalList -> !externalCalList.isEmpty())
                .orElseGet(() -> {
                    List<ExternalCalendarSourceDto> calendarSourceDtos = calendarExternalSourceRetrieveService.retrieveIntegrationCalendarSources(integrationInfoDto);
                    return calendarSourceDtos.stream().map(externalCalendarSourceDto -> {
                                TaskExternalCalendarFilterDto taskExternalCalendarFilterDto = new TaskExternalCalendarFilterDto();
                                taskExternalCalendarFilterDto.setCalendarId(calendarDto.getCalendarId());
                                taskExternalCalendarFilterDto.setCalendarSourceId(externalCalendarSourceDto.getId());
                                return taskExternalCalendarFilterDto;
                            })
                            .toList();
                });
    }

    private List<String> mapRequestedCalendarIds(TaskSearchFilterVo taskSearchFilterVo) {
        log.info("Map requested calendar ids has started.");
        return Optional.of(taskSearchFilterVo)
                .map(TaskSearchFilterVo::getExternalCalendarList)
                .map(Collection::stream)
                .map(calListStream -> calListStream.map(TaskExternalCalendarFilterDto::getCalendarId))
                .map(Stream::toList)
                .orElseGet(Collections::emptyList);
    }

    private List<CalendarEventDto> retrieveExternalSourceEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest) {
        log.info("Retrieve external source events has started. retrieveEventListRequest: {}", retrieveEventListRequest);
        IntegrationCalendarRetrieveStrategy integrationCalendarRetrieveStrategy = integrationCalendarRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        return integrationCalendarRetrieveStrategy.retrieveCalendarEvents(integrationInfoDto, retrieveEventListRequest);
    }
}
