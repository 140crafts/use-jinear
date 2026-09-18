package co.jinear.core.manager.mcp.tool.workspace;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpWorkspaceEnvelopeView;
import co.jinear.core.model.mcp.view.McpWorkspaceView;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpGetWorkspaceInput;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetWorkspaceTool implements McpTool {

    private final WorkspaceManager workspaceManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_workspace")
                .title("Get a workspace")
                .description("Reads one workspace by its id or by its short username handle. "
                             + "Use it to resolve a workspace a person named in conversation into a workspaceId.")
                .input(McpSchemaGenerator.forInput(McpGetWorkspaceInput.class))
                .output(McpSchemaGenerator.single("workspace", "The workspace.", McpWorkspaceView.class))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetWorkspaceInput input = args.bind(McpGetWorkspaceInput.class);
        if (Objects.isNull(input.getWorkspaceId()) && Objects.isNull(input.getUsername())) {
            return McpToolResult.error("Supply either workspaceId or username. Call list_workspaces to see both.");
        }
        WorkspaceBaseResponse response = Objects.nonNull(input.getWorkspaceId())
                ? workspaceManager.retrieveWorkspaceWithId(input.getWorkspaceId())
                : workspaceManager.retrieveWorkspaceWithUsername(input.getUsername());
        context.setWorkspaceId(response.getWorkspace().getWorkspaceId());
        return McpToolResult.of(McpWorkspaceEnvelopeView.of(mcpViewConverter.workspace(response.getWorkspace())));
    }
}
