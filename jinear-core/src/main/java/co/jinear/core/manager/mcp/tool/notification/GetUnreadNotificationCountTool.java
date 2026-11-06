package co.jinear.core.manager.mcp.tool.notification;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpGetUnreadNotificationCountInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpUnreadNotificationCountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUnreadNotificationCountTool implements McpTool {

    private final NotificationEventListingManager notificationEventListingManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_unread_notification_count")
                .title("Count unread notifications")
                .description("Returns how many notifications of the signed in account are unread in a workspace. "
                             + "It does not mark them as read.")
                .input(McpSchemaGenerator.forInput(McpGetUnreadNotificationCountInput.class))
                .output(McpSchemaGenerator.forType(McpUnreadNotificationCountView.class))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetUnreadNotificationCountInput input = args.bind(McpGetUnreadNotificationCountInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        Long count = notificationEventListingManager.retrieveUnreadNotificationCount(input.getWorkspaceId()).getUnreadNotificationCount();
        McpUnreadNotificationCountView view = new McpUnreadNotificationCountView();
        view.setWorkspaceId(input.getWorkspaceId());
        view.setUnreadCount(count);
        return McpToolResult.of(view);
    }
}
