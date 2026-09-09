package co.jinear.core.manager.mcp.tool.workspace;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpMemberView;
import co.jinear.core.model.mcp.view.McpPageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpListWorkspaceMembersInput;

@Service
@RequiredArgsConstructor
public class ListWorkspaceMembersTool implements McpTool {

    private final WorkspaceMemberRetrieveManager workspaceMemberRetrieveManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_workspace_members")
                .title("List workspace members")
                .description("Lists the people in a workspace with their account ids. "
                             + "Use it to turn a person's name into the accountId that create_task and update_task take as an assignee.")
                .input(McpSchemaGenerator.forInput(McpListWorkspaceMembersInput.class))
                .output(McpSchemaGenerator.page(McpMemberView.class, "People in this workspace."))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListWorkspaceMembersInput input = args.bind(McpListWorkspaceMembersInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<WorkspaceMemberDto> page = workspaceMemberRetrieveManager
                .retrieveWorkspaceMembers(input.getWorkspaceId(), args.page())
                .getWorkspaceMemberDtoPage();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::member));
    }
}
