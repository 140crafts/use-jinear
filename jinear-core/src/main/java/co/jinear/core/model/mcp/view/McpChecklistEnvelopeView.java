package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpChecklistEnvelopeView implements McpToolPayload {

    private McpChecklistView checklist;

    public static McpChecklistEnvelopeView of(McpChecklistView checklist) {
        McpChecklistEnvelopeView envelope = new McpChecklistEnvelopeView();
        envelope.setChecklist(checklist);
        return envelope;
    }
}
