package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpActivityViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.workspace.WorkspaceActivityFilterManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpListActivityInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpActivityView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.workspace.WorkspaceActivityFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListActivityTool implements McpTool {

    private static final int MAX_PAGE_SIZE = 25;

    private final WorkspaceActivityFilterManager workspaceActivityFilterManager;
    private final McpActivityViewConverter mcpActivityViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_activity")
                .title("List activity")
                .description("Lists what happened in a workspace, newest first: tasks created, renamed, moved between statuses, "
                             + "reassigned, commented on, checklist and board changes, attachments and members joining. "
                             + "Narrow it to teams or to tasks. "
                             + "This is the tool for questions like what changed this week or what someone did yesterday.")
                .input(McpSchemaGenerator.forInput(McpListActivityInput.class, MAX_PAGE_SIZE))
                .output(McpSchemaGenerator.page(McpActivityView.class, "Activity, newest first."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListActivityInput input = args.bind(McpListActivityInput.class);
        WorkspaceActivityFilterRequest request = new WorkspaceActivityFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setTeamIdList(input.getTeamIds());
        request.setTaskIds(input.getTaskIds());
        request.setPage(args.page());
        request.setSize(args.pageSize(MAX_PAGE_SIZE));
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<WorkspaceActivityDto> activities = workspaceActivityFilterManager.filterActivities(request).getWorkspaceActivityDtoPageDto();
        McpPageView<McpActivityView> view = McpPageView.of(activities, mcpActivityViewConverter::activity);
        return McpToolResult.of(view);
    }
}
