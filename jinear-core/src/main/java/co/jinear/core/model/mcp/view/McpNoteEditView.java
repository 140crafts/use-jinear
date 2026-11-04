package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteEditView implements McpToolPayload {

    @McpField("Note id.")
    private String noteId;

    @McpField("Link that opens the note in the Jinear app.")
    private String url;

    @McpField("False when the note already had this content.")
    private Boolean changed;
}
