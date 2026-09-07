package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskDetailEnvelopeView implements McpToolPayload {

    private McpTaskDetailView task;

    public static McpTaskDetailEnvelopeView of(McpTaskDetailView task) {
        McpTaskDetailEnvelopeView envelope = new McpTaskDetailEnvelopeView();
        envelope.setTask(task);
        return envelope;
    }
}
