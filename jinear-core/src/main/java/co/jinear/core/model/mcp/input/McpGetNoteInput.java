package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetNoteInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = "Note id, from list_notes or search_notes.", hint = "Call list_notes or search_notes to find it.")
    private String noteId;
}
