package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpRenameTaskChecklistInput {

    @NotBlank
    @McpField(value = "Checklist id, from get_task.", hint = "Call get_task to find it.")
    private String checklistId;

    @NotBlank
    @McpField("New checklist title.")
    private String title;
}
