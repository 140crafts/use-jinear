package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskView implements McpToolPayload {

    @McpField("Task id.")
    private String taskId;

    @McpField("Workspace this task belongs to.")
    private String workspaceId;

    @McpField("Team this task belongs to.")
    private String teamId;

    @McpField("One line summary.")
    private String title;

    @McpField("Human readable reference, for example ENG-42.")
    private String reference;

    @McpField("Current status id.")
    private String workflowStatusId;

    @McpField("Current status name.")
    private String workflowStatusName;

    @McpField("BACKLOG, NOT_STARTED, STARTED, COMPLETED or CANCELLED.")
    private String workflowStateGroup;

    @McpField("Account id of the assignee, or null.")
    private String assignedTo;

    @McpField("Account id of whoever created the task.")
    private String ownerId;

    @McpField("ISO 8601 start date, or null.")
    private String assignedDate;

    @McpField("ISO 8601 due date, or null.")
    private String dueDate;

    @McpField("Label applied to this task, or null.")
    private String topicId;
}
