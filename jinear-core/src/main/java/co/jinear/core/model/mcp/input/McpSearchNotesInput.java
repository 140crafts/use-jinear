package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpSearchNotesInput extends McpWorkspaceScopedInput {

    @McpField("Restrict to one notebook, from list_notebooks.")
    private String notebookId;

    @McpField("Restrict to the children of one note.")
    private String parentNoteId;

    @McpField("Words to find in note titles and bodies. Every word must match as a word prefix. "
              + "Omit it to list the notes instead.")
    private String query;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
