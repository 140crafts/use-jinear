package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpSetTaskStatusInput {

    @NotBlank
    @McpField(McpInputDescriptions.TASK_ID)
    private String taskId;

    @NotBlank
    @McpField(value = "Status id, from list_workflow_statuses for the task's team.",
            hint = "Call list_workflow_statuses to find it.")
    private String workflowStatusId;
}
