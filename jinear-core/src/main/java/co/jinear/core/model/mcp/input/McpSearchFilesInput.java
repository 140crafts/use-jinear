package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpSearchFilesInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField("Words to find in file and folder names, original file names, file types and the owner's username. Every word must appear as a whole word or as the start of a word.")
    private String query;

    @McpField("Return only FOLDER entries or only FILE entries. Omit for both.")
    private MaterialType type;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
