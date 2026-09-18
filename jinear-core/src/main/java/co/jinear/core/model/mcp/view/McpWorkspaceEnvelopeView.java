package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpWorkspaceEnvelopeView implements McpToolPayload {

    private McpWorkspaceView workspace;

    public static McpWorkspaceEnvelopeView of(McpWorkspaceView workspace) {
        McpWorkspaceEnvelopeView envelope = new McpWorkspaceEnvelopeView();
        envelope.setWorkspace(workspace);
        return envelope;
    }
}
