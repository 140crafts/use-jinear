package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteVersionView implements McpToolPayload {

    @McpField("Version id.")
    private String versionId;

    @McpField("Account id of the editor.")
    private String editorAccountId;

    @McpField("Editor handle.")
    private String editorUsername;

    @McpField("ISO 8601 instant the version was first saved.")
    private String createdAt;

    @McpField("ISO 8601 instant of the last edit in this version.")
    private String updatedAt;
}
