package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpReplaceInNoteInput extends McpNoteScopedInput {

    @NotBlank
    @Size(max = McpInputDescriptions.NOTE_BODY_MAX_LENGTH)
    @McpField("Exact HTML to replace, from get_note. It must match exactly once.")
    private String oldText;

    @NotNull
    @Size(max = McpInputDescriptions.NOTE_BODY_MAX_LENGTH)
    @McpField("HTML to put in its place. Empty removes oldText.")
    private String newText;
}
