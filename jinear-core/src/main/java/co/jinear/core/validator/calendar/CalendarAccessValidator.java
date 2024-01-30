package co.jinear.core.validator.calendar;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.calendar.CalendarDto;
import co.jinear.core.service.calendar.CalendarMemberRetrieveService;
import co.jinear.core.service.calendar.CalendarRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarAccessValidator {

    private final CalendarMemberRetrieveService calendarMemberRetrieveService;
    private final CalendarRetrieveService calendarRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;

    public void validateHasCalendarAccess(String accountId, String calendarId) {
        boolean isFeedMember = isCalendarMember(accountId, calendarId);
        if (Boolean.FALSE.equals(isFeedMember)) {
            throw new NoAccessException();
        }
    }

    public void validateHasAdminAccess(String accountId, String calendarId) {
        boolean isOwner = isCalendarOwner(accountId, calendarId);
        boolean isWorkspaceAdmin = isCalendarWorkspaceAdmin(accountId, calendarId);
        if (!isOwner || !isWorkspaceAdmin) {
            throw new NoAccessException();
        }
    }

    private boolean isCalendarMember(String accountId, String calendarId) {
        return calendarMemberRetrieveService.isCalendarMember(accountId, calendarId);
    }

    public boolean isCalendarOwner(String accountId, String calendarId) {
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarId);
        return calendarDto.getInitializedBy().equals(accountId);
    }

    private boolean isCalendarWorkspaceAdmin(String accountId, String calendarId) {
        CalendarDto calendarDto = calendarRetrieveService.retrieveCalendar(calendarId);
        String workspaceId = calendarDto.getWorkspaceId();
        return workspaceMemberService.doesAccountHaveWorkspaceAdminAccess(accountId, workspaceId);
    }
}
