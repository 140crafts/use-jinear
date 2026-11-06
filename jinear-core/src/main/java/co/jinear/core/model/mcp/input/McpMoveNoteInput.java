package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpMoveNoteInput extends McpNoteScopedInput {

    @McpField("Target notebook id, from list_notebooks. Leave it out to stay in the current notebook.")
    private String notebookId;

    @McpField("New parent note id. Leave it out to put the note at the top level.")
    private String parentNoteId;
}
