package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import co.jinear.core.model.mcp.schema.McpZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class McpListCalendarEventsInput extends McpWorkspaceScopedInput {

    @NotNull
    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField(value = "Start of the window.", hint = "For example 2026-08-29 or 2026-08-29T14:00:00Z.")
    private ZonedDateTime from;

    @NotNull
    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField(value = "End of the window.", hint = "For example 2026-08-29 or 2026-08-29T14:00:00Z.")
    private ZonedDateTime to;

    @McpField("Restrict to the calendars of these teams.")
    private List<String> teamIds;

    @McpField("Restrict to these calendars.")
    private List<String> calendarIds;
}
