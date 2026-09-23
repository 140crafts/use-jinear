package co.jinear.core.manager.mcp.tool.workspace;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpWorkflowStatusView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import co.jinear.core.model.mcp.input.McpListWorkflowStatusesInput;

@Service
@RequiredArgsConstructor
public class ListWorkflowStatusesTool implements McpTool {

    private final TeamWorkflowStatusManager teamWorkflowStatusManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_workflow_statuses")
                .title("List a team's workflow statuses")
                .description("Lists the workflow statuses a team's tasks can be in, such as Backlog, In Progress or Done. "
                             + "Statuses are defined per team and referenced by id, so read this before calling set_task_status.")
                .input(McpSchemaGenerator.forInput(McpListWorkflowStatusesInput.class))
                .output(McpSchemaGenerator.list(McpWorkflowStatusView.class, "Statuses this team's tasks can be in, in board order."))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListWorkflowStatusesInput input = args.bind(McpListWorkflowStatusesInput.class);
        Map<TeamWorkflowStateGroup, List<TeamWorkflowStatusDto>> grouped =
                teamWorkflowStatusManager.retrieveAllFromTeam(input.getTeamId())
                        .getGroupedTeamWorkflowStatusListDto()
                        .getGroupedTeamWorkflowStatuses();
        return McpToolResult.of(McpListView.of(inBoardOrder(grouped), mcpViewConverter::workflowStatus));
    }

    private List<TeamWorkflowStatusDto> inBoardOrder(Map<TeamWorkflowStateGroup, List<TeamWorkflowStatusDto>> grouped) {
        List<TeamWorkflowStatusDto> statuses = new ArrayList<>(grouped.values().stream().flatMap(List::stream).toList());
        statuses.sort((first, second) -> Integer.compare(order(first), order(second)));
        return statuses;
    }

    private int order(TeamWorkflowStatusDto status) {
        return status.getOrder() == null ? Integer.MAX_VALUE : status.getOrder();
    }
}
