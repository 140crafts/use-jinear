package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNotebookView implements McpToolPayload {

    @McpField("Notebook id.")
    private String notebookId;

    @McpField("Workspace this notebook belongs to.")
    private String workspaceId;

    @McpField("Notebook name.")
    private String title;

    @McpField("Notebook description, or null.")
    private String description;

    @McpField("Who can see this notebook.")
    private String visibility;
}
