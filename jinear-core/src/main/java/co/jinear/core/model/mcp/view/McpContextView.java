package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpContextView implements McpToolPayload {

    @McpField("The signed in account.")
    private McpContextAccountView account;

    @McpField("The workspace and team this account last worked in. Use them when the person does not name one.")
    private McpContextDefaultsView defaults;

    @McpField("OAuth scopes this connection was granted.")
    private List<String> grantedScopes;

    @McpField("Workspaces this account belongs to.")
    private List<McpContextWorkspaceView> workspaces;
}
