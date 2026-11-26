package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteVersionEnvelopeView implements McpToolPayload {

    private McpNoteVersionDetailView version;

    public static McpNoteVersionEnvelopeView of(McpNoteVersionDetailView version) {
        McpNoteVersionEnvelopeView envelope = new McpNoteVersionEnvelopeView();
        envelope.setVersion(version);
        return envelope;
    }
}
