package co.jinear.core.service.calendar;

import co.jinear.core.converter.calendar.CalendarMemberDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.dto.calendar.CalendarMemberDto;
import co.jinear.core.model.entity.calendar.CalendarMember;
import co.jinear.core.repository.calendar.CalendarMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarMemberRetrieveService {

    private final int PAGE_SIZE = 50;

    private final CalendarMemberRepository calendarMemberRepository;
    private final CalendarMemberDtoConverter calendarMemberDtoConverter;
    private final CalendarExternalSourceRetrieveService calendarExternalSourceRetrieveService;

    public CalendarMember retrieveEntity(String accountId, String calendarId) {
        return calendarMemberRepository.findFirstByAccountIdAndCalendarIdAndPassiveIdIsNull(accountId, calendarId)
                .orElseThrow(NotFoundException::new);
    }

    public List<CalendarMemberDto> retrieveAccountCalendarsIncludingExternalSources(String accountId, String workspaceId) {
        log.info("Retrieve account calendars has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return calendarMemberRepository.findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .stream()
                .map(calendarMemberDtoConverter::convert)
                .map(this::retrieveExternalCalendarSources)
                .toList();
    }

    public Page<CalendarMemberDto> findAllByCalendarIdAndPassiveIdIsNull(String calendarId, int page) {
        return calendarMemberRepository.findAllByCalendarIdAndPassiveIdIsNull(calendarId, PageRequest.of(page, PAGE_SIZE))
                .map(calendarMemberDtoConverter::convert);

    }

    public boolean isCalendarMember(String accountId, String calendarId) {
        log.info("Is calendar member has started. accountId: {}, calendarId: {}", accountId, calendarId);
        return calendarMemberRepository.findFirstByAccountIdAndCalendarIdAndPassiveIdIsNull(accountId, calendarId).isPresent();
    }


    private CalendarMemberDto retrieveExternalCalendarSources(CalendarMemberDto calendarMemberDto) {
        CalendarDto calendarDto = calendarMemberDto.getCalendar();
        calendarDto = calendarExternalSourceRetrieveService.retrieveAndSetExternalCalendarSources(calendarDto);
        calendarMemberDto.setCalendar(calendarDto);
        return calendarMemberDto;
    }
}
