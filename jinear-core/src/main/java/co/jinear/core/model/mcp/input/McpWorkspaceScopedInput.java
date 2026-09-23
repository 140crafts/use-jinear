package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = McpInputDescriptions.WORKSPACE_ID, hint = "Call list_workspaces to find it.")
    private String workspaceId;
}
