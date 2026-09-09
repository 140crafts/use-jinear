package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetNoteInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = "Note id, from search_notes.", hint = "Call search_notes to find it.")
    private String noteId;
}
