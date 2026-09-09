package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpListFilesInput extends McpWorkspaceScopedInput {

    @McpField("Folder to list. Omit for the root of the workspace.")
    private String parentFolderId;

    @McpField("Return only FOLDER entries or only FILE entries. Omit for both.")
    private MaterialType type;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
