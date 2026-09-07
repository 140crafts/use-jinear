package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpFileView implements McpToolPayload {

    @McpField("File or folder id.")
    private String materialId;

    @McpField("Workspace this item belongs to.")
    private String workspaceId;

    @McpField("File or folder name.")
    private String name;

    @McpField("FOLDER or FILE.")
    private String materialType;

    @McpField("Containing folder, or null at the root.")
    private String parentMaterialId;

    @McpField("Stored media id for a file, null for a folder.")
    private String mediaId;

    @McpField("Who can reach this item.")
    private String accessType;
}
