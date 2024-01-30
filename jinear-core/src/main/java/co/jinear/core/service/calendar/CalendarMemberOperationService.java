package co.jinear.core.service.calendar;

import co.jinear.core.model.entity.calendar.CalendarMember;
import co.jinear.core.model.vo.calendar.AddCalendarMemberVo;
import co.jinear.core.repository.calendar.CalendarMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarMemberOperationService {

    private final CalendarMemberRepository calendarMemberRepository;
    private final CalendarMemberRetrieveService calendarMemberRetrieveService;

    public void addCalendarMember(AddCalendarMemberVo addCalendarMemberVo) {
        log.info("Add calendar member addCalendarMemberVo: {}", addCalendarMemberVo);
        CalendarMember calendarMember = mapToEntity(addCalendarMemberVo);
        calendarMemberRepository.save(calendarMember);
        log.info("Add calendar member has completed.");
    }

    public void removeCalendarMember(String calendarId, String accountId, String passiveId) {
        log.info("Remove calendar member has started. calendarId: {}, accountId: {}, passiveId: {}", calendarId, accountId, passiveId);
        CalendarMember calendarMember = calendarMemberRetrieveService.retrieveEntity(accountId, calendarId);
        calendarMember.setPassiveId(passiveId);
        calendarMemberRepository.save(calendarMember);
        log.info("Calendar member removed with passiveId: {}", passiveId);
    }

    @Transactional
    public void removeAllMembers(String calendarId, String passiveId) {
        log.info("Remove all members has started. calendarId: {}, passiveId: {}", calendarId, passiveId);
        calendarMemberRepository.removeAllMembers(calendarId, passiveId);
        log.info("All calendar members removed with passiveId: {}", passiveId);
    }


    private CalendarMember mapToEntity(AddCalendarMemberVo addCalendarMemberVo) {
        CalendarMember calendarMember = new CalendarMember();
        calendarMember.setAccountId(addCalendarMemberVo.getAccountId());
        calendarMember.setWorkspaceId(addCalendarMemberVo.getWorkspaceId());
        calendarMember.setCalendarId(addCalendarMemberVo.getCalendarId());
        return calendarMember;
    }
}
