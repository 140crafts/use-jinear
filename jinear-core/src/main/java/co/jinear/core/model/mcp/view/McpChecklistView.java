package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpChecklistView implements McpToolPayload {

    @McpField("Checklist id.")
    private String checklistId;

    @McpField("Task this checklist belongs to.")
    private String taskId;

    @McpField("Checklist title.")
    private String title;

    @McpField("Items, oldest first.")
    private List<McpChecklistItemView> items;
}
