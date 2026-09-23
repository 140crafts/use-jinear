package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpWorkspaceView implements McpToolPayload {

    @McpField("Workspace id. Pass this to any tool that takes workspaceId.")
    private String workspaceId;

    @McpField("Short workspace handle used in Jinear URLs.")
    private String username;

    @McpField("Display name.")
    private String title;

    @McpField("Billing tier, BASIC or PRO.")
    private String tier;
}
