package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNotificationView implements McpToolPayload {

    @McpField("Notification id.")
    private String notificationId;

    @McpField("What the notification is about, for example TASK_CHANGE_ASSIGNEE, TASK_NEW_COMMENT or TASK_REMINDER.")
    private String type;

    @McpField("Notification title.")
    private String title;

    @McpField("Notification text.")
    private String text;

    @McpField("Link that opens the related record in the Jinear app.")
    private String url;

    @McpField("True when it was already read before this call.")
    private Boolean read;

    @McpField("ISO 8601 instant it was created.")
    private String createdAt;
}
