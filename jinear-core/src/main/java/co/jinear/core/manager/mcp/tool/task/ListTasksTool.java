package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.mcp.view.McpTaskView;
import co.jinear.core.model.request.task.TaskFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import co.jinear.core.model.mcp.input.McpListTasksInput;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ListTasksTool implements McpTool {

    private final TaskListingManager taskListingManager;
    private final McpProperties mcpProperties;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_tasks")
                .title("List tasks with filters")
                .description("Lists tasks in a workspace, optionally narrowed by team, assignee, workflow status, "
                             + "state group or a date range. "
                             + "This is the tool for questions like what is in progress, what is assigned to someone, "
                             + "or what is due this week.")
                .input(McpSchemaGenerator.forInput(McpListTasksInput.class, mcpProperties.getMaxPageSize()))
                .output(McpSchemaGenerator.page(McpTaskView.class, "Matching tasks, newest first."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTasksInput input = args.bind(McpListTasksInput.class);
        TaskFilterRequest request = new TaskFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setPage(args.page());
        request.setSize(args.pageSize(mcpProperties.getMaxPageSize()));
        request.setTeamIdList(nullIfEmpty(input.getTeamIds()));
        request.setAssigneeIds(nullIfEmpty(input.getAssigneeIds()));
        request.setWorkflowStatusIdList(nullIfEmpty(input.getWorkflowStatusIds()));
        request.setWorkflowStateGroups(nullIfEmpty(input.getStateGroups()));
        request.setTimespanStart(input.getFrom());
        request.setTimespanEnd(input.getTo());
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<TaskDto> page = taskListingManager.filterTasks(request).getTaskDtoPage();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::task));
    }

    private <T> List<T> nullIfEmpty(List<T> values) {
        return Objects.isNull(values) || values.isEmpty() ? null : values;
    }
}
