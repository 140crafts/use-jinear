package co.jinear.core.model.request.calendar;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEventMoveRequest {
    @NotBlank
    private String calendarId;
    @NotBlank
    private String calendarSourceId;
    @NotBlank
    private String targetCalendarSourceId;
    @NotBlank
    private String eventId;
}
