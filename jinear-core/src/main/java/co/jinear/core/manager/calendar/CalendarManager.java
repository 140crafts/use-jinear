package co.jinear.core.manager.calendar;

import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.calendar.CalendarMemberOperationService;
import co.jinear.core.service.calendar.CalendarOperationService;
import co.jinear.core.service.calendar.CalendarRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final CalendarRetrieveService calendarRetrieveService;
    private final CalendarOperationService calendarOperationService;
    private final CalendarMemberOperationService calendarMemberOperationService;
    private final PassiveService passiveService;

    @Transactional
    public BaseResponse deleteCalendar(String calendarId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarId);
        workspaceValidator.validateHasAccess(currentAccountId, calendarDto.getWorkspaceId());
        //TODO: cgds-302 add calendar member validation
        log.info("Delete calendar has started. calendarId: {}, currentAccountId: {}", calendarId, currentAccountId);
        String passiveId = calendarOperationService.passivizeCalendar(calendarId);
        calendarMemberOperationService.removeAllMembers(calendarId, passiveId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }
}
