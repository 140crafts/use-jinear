package co.jinear.core.manager.mcp.tool.workspace;

import co.jinear.core.converter.mcp.McpContextViewConverter;
import co.jinear.core.manager.account.AccountContextManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.model.dto.account.AccountContextDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpGetContextInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpContextView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetContextTool implements McpTool {

    private final AccountContextManager accountContextManager;
    private final McpContextViewConverter mcpContextViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_context")
                .title("Get the account's working context")
                .description("Returns the signed in account, its default workspace and team, every workspace it belongs to "
                             + "with teams, workflow statuses and topics, and the scopes this connection was granted. "
                             + "Call this first, before any other Jinear tool. Use the ids from here directly instead of "
                             + "calling list_workspaces, list_teams, list_workflow_statuses or list_topics.")
                .input(McpSchemaGenerator.forInput(McpGetContextInput.class))
                .output(McpSchemaGenerator.forType(McpContextView.class))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetContextInput input = args.bind(McpGetContextInput.class);
        boolean includeMembers = Boolean.TRUE.equals(input.getIncludeMembers());
        context.setWorkspaceId(input.getWorkspaceId());
        AccountContextDto accountContextDto = accountContextManager.retrieveAccountContext(input.getWorkspaceId(), includeMembers);
        McpContextView view = mcpContextViewConverter.context(accountContextDto, context.getScopes());
        return McpToolResult.of(view);
    }
}
