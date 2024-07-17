package co.jinear.core.model.request.calendar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
public class CalendarEventInitializeRequest {
    @NotBlank
    private String calendarId;
    @NotBlank
    private String calendarSourceId;
    @Nullable
    private String summary;
    @Nullable
    private String description;
    @Nullable
    private String location;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime assignedDate;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dueDate;
    @Nullable
    private Boolean hasPreciseAssignedDate = Boolean.FALSE;
    @Nullable
    private Boolean hasPreciseDueDate = Boolean.FALSE;
}
