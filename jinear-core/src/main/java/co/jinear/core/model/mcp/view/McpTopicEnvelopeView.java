package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTopicEnvelopeView implements McpToolPayload {

    private McpTopicView topic;

    public static McpTopicEnvelopeView of(McpTopicView topic) {
        McpTopicEnvelopeView envelope = new McpTopicEnvelopeView();
        envelope.setTopic(topic);
        return envelope;
    }
}
