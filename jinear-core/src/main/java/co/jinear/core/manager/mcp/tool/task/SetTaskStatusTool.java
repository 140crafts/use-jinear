package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskEnvelopeView;
import co.jinear.core.model.mcp.view.McpTaskView;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpSetTaskStatusInput;

@Service
@RequiredArgsConstructor
public class SetTaskStatusTool implements McpTool {

    private final TaskUpdateManager taskUpdateManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("set_task_status")
                .title("Move a task to a workflow status")
                .description("Moves a task into one of its team's workflow statuses, for example from In Progress to Done. "
                             + "Statuses are per team and referenced by id, so call list_workflow_statuses for the task's team first. "
                             + "Jinear does not delete tasks; retiring one means moving it to a status in the CANCELLED group.")
                .input(McpSchemaGenerator.forInput(McpSetTaskStatusInput.class))
                .output(McpSchemaGenerator.single("task", "The task in its new status.", McpTaskView.class))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpSetTaskStatusInput input = args.bind(McpSetTaskStatusInput.class);
        TaskResponse response = taskUpdateManager.updateTaskWorkflowStatus(
                input.getTaskId(), input.getWorkflowStatusId());
        context.setWorkspaceId(response.getTaskDto().getWorkspaceId());
        return McpToolResult.of(McpTaskEnvelopeView.of(mcpViewConverter.task(response.getTaskDto())));
    }
}
