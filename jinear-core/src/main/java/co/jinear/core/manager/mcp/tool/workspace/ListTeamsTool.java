package co.jinear.core.manager.mcp.tool.workspace;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpTeamView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import co.jinear.core.model.mcp.input.McpListTeamsInput;

@Service
@RequiredArgsConstructor
public class ListTeamsTool implements McpTool {

    private final TeamRetrieveManager teamRetrieveManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_teams")
                .title("List teams in a workspace")
                .description("Lists the teams inside a workspace. Tasks and boards belong to a team, so a teamId from here "
                             + "is needed before creating a task or reading a team's board.")
                .input(McpSchemaGenerator.forInput(McpListTeamsInput.class))
                .output(McpSchemaGenerator.list(McpTeamView.class, "Teams in this workspace."))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTeamsInput input = args.bind(McpListTeamsInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        List<TeamDto> teams = teamRetrieveManager.retrieveWorkspaceTeams(input.getWorkspaceId()).getTeamDtoList();
        return McpToolResult.of(McpListView.of(teams, mcpViewConverter::team));
    }
}
