package co.jinear.core.model.request.calendar;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class CalendarEventTitleDescriptionUpdateRequest {
    @NotBlank
    private String calendarId;
    @NotBlank
    private String calendarSourceId;
    @NotBlank
    private String calendarEventId;
    @Nullable
    private String title;
    @Nullable
    private String description;
}
