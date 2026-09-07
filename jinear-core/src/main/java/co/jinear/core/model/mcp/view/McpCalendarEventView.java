package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpCalendarEventView implements McpToolPayload {

    @McpField("Event id.")
    private String calendarEventId;

    @McpField("Calendar this event belongs to.")
    private String calendarId;

    @McpField("Workspace this event belongs to.")
    private String workspaceId;

    @McpField("Event title.")
    private String title;

    @McpField("ISO 8601 start.")
    private String startsAt;

    @McpField("ISO 8601 end, or null for an all day event.")
    private String endsAt;

    @McpField("Free text location, or null.")
    private String location;

    @McpField("JINEAR for a native event, GOOGLE_CALENDAR for a synced one, TASK for a task shown on the calendar.")
    private String sourceType;

    @McpField("Task this event mirrors, or null.")
    private String relatedTaskId;
}
