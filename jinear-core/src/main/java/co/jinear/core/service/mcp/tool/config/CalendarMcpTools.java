package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Calendar reads.
 * <p>
 * There is no calendar write tool. Jinear has no calendar events of its own: what the
 * calendar shows is tasks that carry dates, plus events synced from a Google Calendar the
 * user attached. Writing an event would mean writing into Google on the user's behalf,
 * which is a third party API this connector does not proxy. Putting work on the calendar
 * is create_task with a dueDate.
 */
@Configuration
@RequiredArgsConstructor
public class CalendarMcpTools {

    private final CalendarEventManager calendarEventManager;

    @Bean
    public McpTool listCalendarEventsTool() {
        return SimpleMcpTool.named("list_calendar_events")
                .title("List calendar events in a date range")
                .description("Lists everything on a workspace's calendar between two instants: tasks that carry dates "
                        + "and events synced from an attached Google Calendar. "
                        + "Use it to answer what is happening this week or when someone is busy.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("from", "Start of the window, as an ISO 8601 instant.")
                        .requiredString("to", "End of the window, as an ISO 8601 instant.")
                        .stringArray("teamIds", "Restrict to the calendars of these teams.", false)
                        .stringArray("calendarIds", "Restrict to these calendars.", false)
                        .build())
                .output(McpShapes.listSchema("Events and dated tasks in the window.", McpShapes.calendarEventSchema()))
                .readOnly()
                .scopes(OauthScope.CALENDAR_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    CalendarEventFilterRequest request = new CalendarEventFilterRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setTimespanStart(args.requiredZonedDateTime("from"));
                    request.setTimespanEnd(args.requiredZonedDateTime("to"));
                    request.setTeamIdList(nullIfEmpty(args.optionalStringList("teamIds")));
                    request.setCalendarIdList(nullIfEmpty(args.optionalStringList("calendarIds")));
                    if (request.getTimespanEnd().isBefore(request.getTimespanStart())) {
                        return McpToolResult.error("to must be at or after from. Received from "
                                + args.requiredString("from") + " and to " + args.requiredString("to") + ".");
                    }
                    context.setWorkspaceId(request.getWorkspaceId());
                    var events = calendarEventManager.filterCalendarEvents(request).getCalendarEventDtoList();
                    return McpToolResult.of(McpShapes.list(events, McpShapes::calendarEvent));
                })
                .build();
    }

    private List<String> nullIfEmpty(List<String> values) {
        return values.isEmpty() ? null : values;
    }
}
