package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarEventUpdateManager;
import co.jinear.core.model.request.calendar.CalendarEventDateUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/calendar/event/update/from-external")
public class CalendarEventUpdateController {

    private final CalendarEventUpdateManager calendarEventUpdateManager;

    @PutMapping("/update-dates")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDates(@Valid @RequestBody CalendarEventDateUpdateRequest calendarEventFilterRequest) {
        return calendarEventUpdateManager.updateEventDate(calendarEventFilterRequest);
    }
}
