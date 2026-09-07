package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskEnvelopeView implements McpToolPayload {

    private McpTaskView task;

    public static McpTaskEnvelopeView of(McpTaskView task) {
        McpTaskEnvelopeView envelope = new McpTaskEnvelopeView();
        envelope.setTask(task);
        return envelope;
    }
}
