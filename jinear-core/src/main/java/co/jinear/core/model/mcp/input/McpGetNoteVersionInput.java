package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpGetNoteVersionInput extends McpNoteScopedInput {

    @NotBlank
    @McpField(value = "Version id, from list_note_versions.", hint = "Call list_note_versions to find it.")
    private String versionId;
}
