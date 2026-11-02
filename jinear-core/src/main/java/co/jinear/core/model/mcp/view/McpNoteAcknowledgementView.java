package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteAcknowledgementView implements McpToolPayload {

    @McpField("Id of the note.")
    private String noteId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpNoteAcknowledgementView of(String noteId) {
        McpNoteAcknowledgementView view = new McpNoteAcknowledgementView();
        view.setNoteId(noteId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
