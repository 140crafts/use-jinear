package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpCreateNoteInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField("Note title.")
    private String title;

    @Size(max = McpInputDescriptions.NOTE_BODY_MAX_LENGTH)
    @McpField("Body as HTML. Allowed: headings, paragraphs, marks, links, lists, task lists, code blocks, tables, images.")
    private String body;

    @McpField("Notebook id, from list_notebooks. Leave it out to make a note with no notebook.")
    private String notebookId;

    @McpField("Parent note id. It must be in the same notebook.")
    private String parentNoteId;
}
