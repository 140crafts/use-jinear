package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpUpdateChecklistItemInput {

    @NotBlank
    @McpField(value = "Checklist item id, from get_task.", hint = "Call get_task to find it.")
    private String checklistItemId;

    @McpField("New item text.")
    private String label;

    @McpField("True to check the item, false to uncheck it.")
    private Boolean checked;
}
