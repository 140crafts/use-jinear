package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpActivityView implements McpToolPayload {

    @McpField("Activity id.")
    private String activityId;

    @McpField("What happened, for example TASK_INITIALIZED, TASK_UPDATE_WORKFLOW_STATUS, TASK_CHANGE_ASSIGNEE, TASK_NEW_COMMENT or CHECKLIST_ITEM_CHECKED_STATUS_CHANGED.")
    private String type;

    @McpField("ISO 8601 instant it happened.")
    private String createdAt;

    @McpField("Account id of whoever did it.")
    private String performedBy;

    @McpField("Handle of whoever did it.")
    private String performedByUsername;

    @McpField("Workspace it happened in.")
    private String workspaceId;

    @McpField("Team it happened in.")
    private String teamId;

    @McpField("Task it happened on, or null.")
    private String taskId;

    @McpField("Task reference and title, for example [ENG-42] Ship the release.")
    private String taskTitle;

    @McpField("Link that opens the task in the Jinear app.")
    private String taskUrl;

    @McpField("Value before the change, for example the old status name or the old assignee handle. Null when there is no short text, such as a body edit.")
    private String oldValue;

    @McpField("Value after the change.")
    private String newValue;

    @McpField("What the change was about when it is not the task itself: a checklist, checklist item, board, attachment, related task or member.")
    private String subject;
}
