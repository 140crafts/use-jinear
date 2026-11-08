package co.jinear.core.manager.mcp.tool.notification;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpListNotificationsInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNotificationView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ListNotificationsTool implements McpTool {

    private final NotificationEventListingManager notificationEventListingManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_notifications")
                .title("List notifications")
                .description("Lists the signed in account's notifications in a workspace, newest first: assignments, comments, "
                             + "status changes and reminders on tasks the account follows. "
                             + "Like opening the inbox in the Jinear app, this marks the listed workspace's or team's notifications as read. "
                             + "Use get_unread_notification_count to check for new ones without reading them.")
                .input(McpSchemaGenerator.forInput(McpListNotificationsInput.class))
                .output(McpSchemaGenerator.page(McpNotificationView.class, "Notifications, newest first."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListNotificationsInput input = args.bind(McpListNotificationsInput.class);
        int page = args.page();
        context.setWorkspaceId(input.getWorkspaceId());
        NotificationEventListingResponse response = retrieve(input, page);
        PageDto<NotificationEventDto> notifications = response.getEventDtoPage();
        McpPageView<McpNotificationView> view = McpPageView.of(notifications, mcpViewConverter::notification);
        return McpToolResult.of(view);
    }

    private NotificationEventListingResponse retrieve(McpListNotificationsInput input, int page) {
        if (Objects.isNull(input.getTeamId())) {
            return notificationEventListingManager.retrieveNotifications(input.getWorkspaceId(), page);
        }
        return notificationEventListingManager.retrieveTeamNotifications(input.getWorkspaceId(), input.getTeamId(), page);
    }
}
