package co.jinear.core.manager.mcp.tool.workspace;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpWorkspaceMembershipView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import co.jinear.core.model.mcp.input.McpListWorkspacesInput;

@Service
@RequiredArgsConstructor
public class ListWorkspacesTool implements McpTool {

    private final WorkspaceManager workspaceManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_workspaces")
                .title("List workspaces")
                .description("Lists every Jinear workspace the signed in account belongs to, with the account's role in each. "
                             + "Call this first: workspaceId is required by almost every other Jinear tool.")
                .input(McpSchemaGenerator.forInput(McpListWorkspacesInput.class))
                .output(McpSchemaGenerator.list(McpWorkspaceMembershipView.class, "The workspaces this account belongs to."))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        List<DetailedWorkspaceMemberDto> workspaces = workspaceManager
                .retrieveAccountWorkspacesInternal(context.getAccountId())
                .getWorkspaces();
        return McpToolResult.of(McpListView.of(workspaces, mcpViewConverter::workspaceMembership));
    }
}
