package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteView implements McpToolPayload {

    @McpField("Note id.")
    private String noteId;

    @McpField("Notebook this note lives in.")
    private String notebookId;

    @McpField("Workspace this note belongs to.")
    private String workspaceId;

    @McpField("Note title.")
    private String title;

    @McpField("Parent note when the note is nested, otherwise null.")
    private String parentNoteId;

    @McpField("Account id of the author.")
    private String ownerId;
}
