package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.mcp.view.McpTaskView;
import co.jinear.core.model.request.task.TaskSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import co.jinear.core.model.mcp.input.McpSearchTasksInput;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SearchTasksTool implements McpTool {

    private final TaskSearchManager taskSearchManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("search_tasks")
                .title("Search tasks by text")
                .description("Finds tasks in a workspace whose title or body matches a plain language query. "
                             + "Use this when the person names a task by what it is about rather than by its reference. "
                             + "Tasks created or edited in the last minute may not appear yet; use list_tasks to see them. "
                             + "Use list_tasks instead when filtering by status, assignee or dates.")
                .input(McpSchemaGenerator.forInput(McpSearchTasksInput.class))
                .output(McpSchemaGenerator.page(McpTaskView.class, "Matching tasks, best match first."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpSearchTasksInput input = args.bind(McpSearchTasksInput.class);
        TaskSearchRequest request = new TaskSearchRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setQuery(input.getQuery());
        request.setTeamIdList(nullIfEmpty(input.getTeamIds()));
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<TaskDto> page = taskSearchManager.searchTask(request, args.page()).getResult();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::task));

    }

    private <T> List<T> nullIfEmpty(List<T> values) {
        return Objects.isNull(values) || values.isEmpty() ? null : values;
    }
}
