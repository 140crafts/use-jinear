package co.jinear.core.service.calendar;

import co.jinear.core.model.entity.calendar.Calendar;
import co.jinear.core.model.vo.calendar.AddCalendarMemberVo;
import co.jinear.core.model.vo.calendar.InitializeCalendarVo;
import co.jinear.core.repository.calendar.CalendarRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarOperationService {

    private final CalendarRepository calendarRepository;
    private final CalendarMemberOperationService calendarMemberOperationService;
    private final CalendarRetrieveService calendarRetrieveService;
    private final PassiveService passiveService;

    public void initializeCalendar(InitializeCalendarVo initializeCalendarVo) {
        log.info("Initialize calendar has started. initializeCalendarVo: {}", initializeCalendarVo);
        Calendar calendar = createCalendar(initializeCalendarVo);
        addInitialCalendarMember(calendar);
        log.info("Initialize calendar has completed.");
    }

    public String passivizeCalendar(String calendarId) {
        log.info("Passivize calendar has started. calendarId: {}", calendarId);
        Calendar calendar = calendarRetrieveService.retrieveEntity(calendarId);
        String passiveId = passiveService.createUserActionPassive();
        calendar.setPassiveId(passiveId);
        log.info("Calendar saved with passiveId: {}", passiveId);
        return passiveId;
    }

    private void addInitialCalendarMember(Calendar calendar) {
        AddCalendarMemberVo addCalendarMemberVo = new AddCalendarMemberVo();
        addCalendarMemberVo.setAccountId(calendar.getInitializedBy());
        addCalendarMemberVo.setWorkspaceId(calendar.getWorkspaceId());
        addCalendarMemberVo.setCalendarId(calendar.getCalendarId());
        calendarMemberOperationService.addCalendarMember(addCalendarMemberVo);
    }

    private Calendar createCalendar(InitializeCalendarVo initializeCalendarVo) {
        Calendar calendar = mapToEntity(initializeCalendarVo);
        return calendarRepository.save(calendar);
    }

    private Calendar mapToEntity(InitializeCalendarVo initializeCalendarVo) {
        Calendar calendar = new Calendar();
        calendar.setWorkspaceId(initializeCalendarVo.getWorkspaceId());
        calendar.setInitializedBy(initializeCalendarVo.getInitializedBy());
        calendar.setIntegrationInfoId(initializeCalendarVo.getIntegrationInfoId());
        calendar.setName(initializeCalendarVo.getName());
        return calendar;
    }
}
