package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpListNotebooksInput extends McpWorkspaceScopedInput {

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
