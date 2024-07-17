package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarEventUpdateManager;
import co.jinear.core.model.request.calendar.CalendarEventDateUpdateRequest;
import co.jinear.core.model.request.calendar.CalendarEventInitializeRequest;
import co.jinear.core.model.request.calendar.CalendarEventMoveRequest;
import co.jinear.core.model.request.calendar.CalendarEventTitleDescriptionUpdateRequest;
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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse insert(@Valid @RequestBody CalendarEventInitializeRequest calendarEventInitializeRequest) {
        return calendarEventUpdateManager.insertEvent(calendarEventInitializeRequest);
    }

    @PostMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse move(@Valid @RequestBody CalendarEventMoveRequest calendarEventMoveRequest) {
        return calendarEventUpdateManager.moveEvent(calendarEventMoveRequest);
    }

    @PutMapping("/dates")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDates(@Valid @RequestBody CalendarEventDateUpdateRequest calendarEventDateUpdateRequest) {
        return calendarEventUpdateManager.updateEventDate(calendarEventDateUpdateRequest);
    }

    @PutMapping("/title-description")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitleAndDescription(@Valid @RequestBody CalendarEventTitleDescriptionUpdateRequest calendarEventTitleDescriptionUpdateRequest) {
        return calendarEventUpdateManager.updateTitleAndDescription(calendarEventTitleDescriptionUpdateRequest);
    }
}
