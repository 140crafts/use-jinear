package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpChecklistItemEnvelopeView implements McpToolPayload {

    private McpChecklistItemView checklistItem;

    public static McpChecklistItemEnvelopeView of(McpChecklistItemView checklistItem) {
        McpChecklistItemEnvelopeView envelope = new McpChecklistItemEnvelopeView();
        envelope.setChecklistItem(checklistItem);
        return envelope;
    }
}
