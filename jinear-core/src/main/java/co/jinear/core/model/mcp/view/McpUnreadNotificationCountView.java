package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpUnreadNotificationCountView implements McpToolPayload {

    @McpField("Workspace the count is for.")
    private String workspaceId;

    @McpField("Number of unread notifications of the signed in account.")
    private Long unreadCount;
}
