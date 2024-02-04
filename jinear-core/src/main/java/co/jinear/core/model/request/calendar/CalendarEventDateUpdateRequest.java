package co.jinear.core.model.request.calendar;

import co.jinear.core.model.enumtype.calendar.CalendarEventSourceType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
public class CalendarEventDateUpdateRequest {
    @NotNull
    private CalendarEventSourceType type;
    @NotNull
    private String calendarEventId;
    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime assignedDate;
    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dueDate;
    @Nullable
    private Boolean hasPreciseAssignedDate;
    @Nullable
    private Boolean hasPreciseDueDate;
}
