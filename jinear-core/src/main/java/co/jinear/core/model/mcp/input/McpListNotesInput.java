package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpListNotesInput extends McpWorkspaceScopedInput {

    @McpField("Restrict to one notebook, from list_notebooks. Omit it to cover every notebook and your drafts.")
    private String notebookId;

    @McpField("Restrict to the children of one note. Omit it to include notes at every level.")
    private String parentNoteId;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;
}
