package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpChecklistItemView implements McpToolPayload {

    @McpField("Checklist item id.")
    private String checklistItemId;

    @McpField("Checklist this item belongs to.")
    private String checklistId;

    @McpField("Item text.")
    private String label;

    @McpField("True when the item is checked.")
    private Boolean checked;
}
