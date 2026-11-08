package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskRelationManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpRemoveTaskRelationInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskRelationAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveTaskRelationTool implements McpTool {

    private final TaskRelationManager taskRelationManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("remove_task_relation")
                .title("Remove a task relation")
                .description("Removes a relation between two tasks. Both tasks stay as they are.")
                .input(McpSchemaGenerator.forInput(McpRemoveTaskRelationInput.class))
                .output(McpSchemaGenerator.acknowledgement("taskRelationId", "The relation that was removed."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpRemoveTaskRelationInput input = args.bind(McpRemoveTaskRelationInput.class);
        taskRelationManager.deleteTaskRelation(input.getTaskRelationId());
        return McpToolResult.of(McpTaskRelationAcknowledgementView.of(input.getTaskRelationId()));
    }
}
