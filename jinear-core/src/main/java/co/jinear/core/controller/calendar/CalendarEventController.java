package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.calendar.CalendarEventListingResponse;
import co.jinear.core.model.response.calendar.CalendarShareableKeyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/calendar/event")
public class CalendarEventController {

    private final CalendarEventManager calendarEventManager;

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public CalendarEventListingResponse filter(@Valid @RequestBody CalendarEventFilterRequest calendarEventFilterRequest) {
        return calendarEventManager.filterCalendarEvents(calendarEventFilterRequest);
    }

    @GetMapping("/exports/workspace/{workspaceId}/key")
    @ResponseStatus(HttpStatus.OK)
    public CalendarShareableKeyResponse retrieveShareableKey(@PathVariable String workspaceId) {
        return calendarEventManager.retrieveShareableKey(workspaceId);
    }

    @PostMapping("/exports/workspace/{workspaceId}/key/refresh")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse refreshShareableKey(@PathVariable String workspaceId) {
        return calendarEventManager.refreshShareableKey(workspaceId);
    }

    @GetMapping("/exports/{shareableKey}")
    @ResponseStatus(HttpStatus.OK)
    public String exportWorkspaceCalendarEvents(@PathVariable String shareableKey) {
        return calendarEventManager.exportWorkspaceCalendarEvents(shareableKey);
    }
}
