package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetFileLinkInput {

    @NotBlank
    @McpField(value = "File id, from list_files. Must be a FILE, not a FOLDER.", hint = "Call list_files to find it.")
    private String materialId;
}
