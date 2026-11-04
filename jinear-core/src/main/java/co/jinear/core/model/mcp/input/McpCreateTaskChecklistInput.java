package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpCreateTaskChecklistInput {

    @NotBlank
    @McpField(value = "Task id, from search_tasks, list_tasks or get_task.", hint = "Call search_tasks to find it.")
    private String taskId;

    @NotBlank
    @McpField("Checklist title.")
    private String title;

    @McpField("Text of the first item.")
    private String firstItemLabel;
}
