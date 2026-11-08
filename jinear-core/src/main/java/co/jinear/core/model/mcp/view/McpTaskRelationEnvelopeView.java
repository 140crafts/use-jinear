package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskRelationEnvelopeView implements McpToolPayload {

    private McpTaskRelationView relation;

    public static McpTaskRelationEnvelopeView of(McpTaskRelationView relation) {
        McpTaskRelationEnvelopeView envelope = new McpTaskRelationEnvelopeView();
        envelope.setRelation(relation);
        return envelope;
    }
}
