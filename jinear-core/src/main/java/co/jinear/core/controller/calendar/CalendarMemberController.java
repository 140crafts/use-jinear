package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.calendar.CalendarMemberListingResponse;
import co.jinear.core.model.response.calendar.CalendarMemberPaginatedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/calendar/member")
public class CalendarMemberController {

    private final CalendarMemberManager calendarMemberManager;

    @GetMapping("/memberships/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public CalendarMemberListingResponse retrieveMemberships(@PathVariable String workspaceId) {
        return calendarMemberManager.retrieveMemberCalendars(workspaceId);
    }

    @GetMapping("/list/{calendarId}")
    @ResponseStatus(HttpStatus.OK)
    public CalendarMemberPaginatedResponse retrieveCalendarMembers(@PathVariable String calendarId,
                                                                   @RequestParam(required = false, defaultValue = "0") int page) {
        return calendarMemberManager.retrieveCalendarMembers(calendarId, page);
    }

    @PostMapping("/{calendarId}/manage/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse addCalendarMember(@PathVariable String calendarId,
                                          @PathVariable String accountId) {
        return calendarMemberManager.addCalendarMember(calendarId, accountId);
    }

    @DeleteMapping("/{calendarId}/manage/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse kickCalendarMember(@PathVariable String calendarId,
                                           @PathVariable String accountId) {
        return calendarMemberManager.kickCalendarMember(calendarId, accountId);
    }
}
