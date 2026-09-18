package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetWorkspaceInput {

    @McpField("Workspace id. Supply this or username.")
    private String workspaceId;

    @McpField("Short workspace handle, as it appears in a Jinear URL. Supply this or workspaceId.")
    private String username;
}
