package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskDetailEnvelopeView;
import co.jinear.core.model.mcp.view.McpTaskDetailView;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpGetTaskInput;

@Service
@RequiredArgsConstructor
public class GetTaskTool implements McpTool {

    private final TaskRetrieveManager taskRetrieveManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_task")
                .title("Get one task")
                .description("Reads a single task in full, including its body, by its human readable reference such as ENG-42. "
                             + "Use it after search_tasks or list_tasks when the body of a specific task is needed.")
                .input(McpSchemaGenerator.forInput(McpGetTaskInput.class))
                .output(McpSchemaGenerator.single("task", "The task, with its body.", McpTaskDetailView.class))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetTaskInput input = args.bind(McpGetTaskInput.class);
        TaskResponse response = taskRetrieveManager.retrieveWithWorkspaceNameAndTeamTagNo(
                input.getWorkspaceUsername(), input.getTeamTag(), input.getTaskNumber());
        context.setWorkspaceId(response.getTaskDto().getWorkspaceId());
        return McpToolResult.of(McpTaskDetailEnvelopeView.of(mcpViewConverter.taskDetail(response.getTaskDto())));
    }
}
