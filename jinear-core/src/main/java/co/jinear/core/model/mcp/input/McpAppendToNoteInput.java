package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.note.NoteAppendPosition;
import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpAppendToNoteInput extends McpNoteScopedInput {

    @NotBlank
    @Size(max = McpInputDescriptions.NOTE_BODY_MAX_LENGTH)
    @McpField("Body as HTML. Allowed: headings, paragraphs, marks, links, lists, task lists, code blocks, tables, images.")
    private String body;

    @McpField("Where to add the body. Defaults to END.")
    private NoteAppendPosition position;
}
