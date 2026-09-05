package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WorkspaceMcpTools {

    private final WorkspaceManager workspaceManager;
    private final WorkspaceMemberRetrieveManager workspaceMemberRetrieveManager;
    private final TeamRetrieveManager teamRetrieveManager;
    private final TeamWorkflowStatusManager teamWorkflowStatusManager;
    private final McpProperties mcpProperties;

    @Bean
    public McpTool listWorkspacesTool() {
        return SimpleMcpTool.named("list_workspaces")
                .title("List workspaces")
                .description("Lists every Jinear workspace the signed in account belongs to, with the account's role in each. "
                        + "Call this first: workspaceId is required by almost every other Jinear tool.")
                .input(McpJsonSchema.noArguments())
                .output(McpShapes.listSchema("The workspaces this account belongs to.", McpShapes.workspaceSchema()))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    var workspaces = workspaceManager.retrieveAccountWorkspacesInternal(context.getAccountId()).getWorkspaces();
                    return McpToolResult.of(McpShapes.list(workspaces, McpShapes::workspaceMembership));
                })
                .build();
    }

    @Bean
    public McpTool getWorkspaceTool() {
        return SimpleMcpTool.named("get_workspace")
                .title("Get a workspace")
                .description("Reads one workspace by its id or by its short username handle. "
                        + "Use it to resolve a workspace a person named in conversation into a workspaceId.")
                .input(McpJsonSchema.object()
                        .string("workspaceId", "Workspace id. Supply this or username.")
                        .string("username", "Short workspace handle, as it appears in a Jinear URL. Supply this or workspaceId.")
                        .build())
                .output(McpShapes.singleSchema("workspace", "The workspace.", McpShapes.workspaceSchema()))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String workspaceId = args.optionalString("workspaceId", null);
                    String username = args.optionalString("username", null);
                    if (workspaceId == null && username == null) {
                        return McpToolResult.error("Supply either workspaceId or username. Call list_workspaces to see both.");
                    }
                    var response = workspaceId != null
                            ? workspaceManager.retrieveWorkspaceWithId(workspaceId)
                            : workspaceManager.retrieveWorkspaceWithUsername(username);
                    context.setWorkspaceId(response.getWorkspace().getWorkspaceId());
                    return McpToolResult.of(McpShapes.single("workspace", McpShapes.workspace(response.getWorkspace())));
                })
                .build();
    }

    @Bean
    public McpTool listTeamsTool() {
        return SimpleMcpTool.named("list_teams")
                .title("List teams in a workspace")
                .description("Lists the teams inside a workspace. Tasks and boards belong to a team, so a teamId from here "
                        + "is needed before creating a task or reading a team's board.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .build())
                .output(McpShapes.listSchema("Teams in this workspace.", McpShapes.teamSchema()))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    String workspaceId = McpToolArguments.of(arguments).requiredString("workspaceId");
                    context.setWorkspaceId(workspaceId);
                    var teams = teamRetrieveManager.retrieveWorkspaceTeams(workspaceId).getTeamDtoList();
                    return McpToolResult.of(McpShapes.list(teams, McpShapes::team));
                })
                .build();
    }

    @Bean
    public McpTool listWorkflowStatusesTool() {
        return SimpleMcpTool.named("list_workflow_statuses")
                .title("List a team's workflow statuses")
                .description("Lists the workflow statuses a team's tasks can be in, such as Backlog, In Progress or Done. "
                        + "Statuses are defined per team and referenced by id, so read this before calling set_task_status.")
                .input(McpJsonSchema.object()
                        .requiredString("teamId", "Team id, from list_teams.")
                        .build())
                .output(McpShapes.listSchema("Statuses this team's tasks can be in, in board order.",
                        McpShapes.workflowStatusSchema()))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    String teamId = McpToolArguments.of(arguments).requiredString("teamId");
                    var grouped = teamWorkflowStatusManager.retrieveAllFromTeam(teamId)
                            .getGroupedTeamWorkflowStatusListDto()
                            .getGroupedTeamWorkflowStatuses();
                    var statuses = new ArrayList<>(grouped.values().stream().flatMap(List::stream).toList());
                    statuses.sort((first, second) -> {
                        int firstOrder = first.getOrder() == null ? Integer.MAX_VALUE : first.getOrder();
                        int secondOrder = second.getOrder() == null ? Integer.MAX_VALUE : second.getOrder();
                        return Integer.compare(firstOrder, secondOrder);
                    });
                    return McpToolResult.of(McpShapes.list(statuses, McpShapes::workflowStatus));
                })
                .build();
    }

    @Bean
    public McpTool listWorkspaceMembersTool() {
        return SimpleMcpTool.named("list_workspace_members")
                .title("List workspace members")
                .description("Lists the people in a workspace with their account ids. "
                        + "Use it to turn a person's name into the accountId that create_task and update_task take as an assignee.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .withPaging(50)
                        .build())
                .output(McpShapes.pageSchema("People in this workspace.", McpShapes.memberSchema()))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String workspaceId = args.requiredString("workspaceId");
                    context.setWorkspaceId(workspaceId);
                    var page = workspaceMemberRetrieveManager
                            .retrieveWorkspaceMembers(workspaceId, args.page())
                            .getWorkspaceMemberDtoPage();
                    return McpToolResult.of(McpShapes.page(page, McpShapes::member));
                })
                .build();
    }
}
