package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteEnvelopeView implements McpToolPayload {

    private McpNoteDetailView note;

    public static McpNoteEnvelopeView of(McpNoteDetailView note) {
        McpNoteEnvelopeView envelope = new McpNoteEnvelopeView();
        envelope.setNote(note);
        return envelope;
    }
}
