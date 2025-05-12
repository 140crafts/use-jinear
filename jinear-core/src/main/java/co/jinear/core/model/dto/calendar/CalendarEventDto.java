package co.jinear.core.model.dto.calendar;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.calendar.CalendarEventSourceType;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class CalendarEventDto {

    private String workspaceId;
    private String calendarId;
    private String calendarEventId;
    private String title;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Boolean hasPreciseAssignedDate = Boolean.FALSE;
    private Boolean hasPreciseDueDate = Boolean.FALSE;
    private CalendarEventSourceType calendarEventSourceType;
    @Nullable
    private RichTextDto description;
    @Nullable
    private String location;
    @Nullable
    private String externalLink;
    @Nullable
    private ExternalCalendarSourceDto externalCalendarSourceDto;
    @Nullable
    private TaskDto relatedTask;
    @Nullable
    private GoogleCalendarEventInfo relatedGoogleCalendarEventInfo;
}
