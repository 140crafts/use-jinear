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

    @McpField("Case insensitive phrase to match against note titles.")
    private String titleContains;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
