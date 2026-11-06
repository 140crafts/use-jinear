package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteEditEnvelopeView implements McpToolPayload {

    private McpNoteEditView edit;

    public static McpNoteEditEnvelopeView of(McpNoteEditView edit) {
        McpNoteEditEnvelopeView envelope = new McpNoteEditEnvelopeView();
        envelope.setEdit(edit);
        return envelope;
    }
}
