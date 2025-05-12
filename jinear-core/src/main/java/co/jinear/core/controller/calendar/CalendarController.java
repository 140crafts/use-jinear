package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/calendar")
public class CalendarController {

    private final CalendarManager calendarManager;

    @DeleteMapping("/{calendarId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteCalendar(@PathVariable String calendarId) {
        return calendarManager.deleteCalendar(calendarId);
    }
}
