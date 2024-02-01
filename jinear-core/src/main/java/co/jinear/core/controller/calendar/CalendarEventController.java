package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.response.calendar.CalendarEventListingResponse;
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
}
