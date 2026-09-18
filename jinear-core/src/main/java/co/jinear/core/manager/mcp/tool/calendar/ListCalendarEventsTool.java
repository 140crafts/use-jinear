package co.jinear.core.manager.mcp.tool.calendar;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpCalendarEventView;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import co.jinear.core.model.mcp.input.McpListCalendarEventsInput;

@Service
@RequiredArgsConstructor
public class ListCalendarEventsTool implements McpTool {

    private final CalendarEventManager calendarEventManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_calendar_events")
                .title("List calendar events in a date range")
                .description("Lists everything on a workspace's calendar between two instants: tasks that carry dates "
                             + "and events synced from an attached Google Calendar. "
                             + "Use it to answer what is happening this week or when someone is busy.")
                .input(McpSchemaGenerator.forInput(McpListCalendarEventsInput.class))
                .output(McpSchemaGenerator.list(McpCalendarEventView.class, "Events and dated tasks in the window."))
                .readOnly()
                .scopes(OauthScope.CALENDAR_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListCalendarEventsInput input = args.bind(McpListCalendarEventsInput.class);
        if (input.getTo().isBefore(input.getFrom())) {
            return McpToolResult.error("to must be at or after from. Received from "
                                       + input.getFrom() + " and to " + input.getTo() + ".");
        }
        CalendarEventFilterRequest request = new CalendarEventFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setTimespanStart(input.getFrom());
        request.setTimespanEnd(input.getTo());
        request.setTeamIdList(nullIfEmpty(input.getTeamIds()));
        request.setCalendarIdList(nullIfEmpty(input.getCalendarIds()));
        context.setWorkspaceId(input.getWorkspaceId());
        List<CalendarEventDto> events = calendarEventManager.filterCalendarEvents(request).getCalendarEventDtoList();
        return McpToolResult.of(McpListView.of(events, mcpViewConverter::calendarEvent));
    }

    private List<String> nullIfEmpty(List<String> values) {
        return Objects.isNull(values) || values.isEmpty() ? null : values;
    }
}
