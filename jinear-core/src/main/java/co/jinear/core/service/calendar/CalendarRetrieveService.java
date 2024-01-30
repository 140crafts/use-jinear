package co.jinear.core.service.calendar;

import co.jinear.core.converter.calendar.CalendarDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.entity.calendar.Calendar;
import co.jinear.core.repository.calendar.CalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarRetrieveService {

    private final CalendarRepository calendarRepository;
    private final CalendarDtoConverter calendarDtoConverter;

    public Calendar retrieveEntity(String calendarId) {
        log.info("Retrieve calendar entity has started. calendarId: {}", calendarId);
        return calendarRepository.findByCalendarIdAndPassiveIdIsNull(calendarId)
                .orElseThrow(NotFoundException::new);
    }

    public CalendarDto retrieveCalendar(String calendarId) {
        log.info("Retrieve feed has started. calendarId: {}", calendarId);
        return calendarRepository.findByCalendarIdAndPassiveIdIsNull(calendarId)
                .map(calendarDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Boolean checkCalendarExist(String workspaceId, String accountId, String integrationId) {
        log.info("Check calendar exists has started. workspaceId: {}, accountId: {}, integrationId: {}", workspaceId, accountId, integrationId);
        return calendarRepository.countAllByWorkspaceIdAndInitializedByAndIntegrationInfoIdAndPassiveIdIsNull(workspaceId, accountId, integrationId) > 0L;
    }
}
