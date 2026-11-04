package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskRelationManager;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpAddTaskRelationInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskRelationEnvelopeView;
import co.jinear.core.model.mcp.view.McpTaskRelationView;
import co.jinear.core.model.request.task.TaskRelationInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddTaskRelationTool implements McpTool {

    private final TaskRelationManager taskRelationManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("add_task_relation")
                .title("Relate two tasks")
                .description("Links two tasks of the same team. SUBTASK makes taskId a subtask of relatedTaskId; "
                             + "BLOCKS and IS_BLOCKED_BY mark a dependency. Returns the relation with its id.")
                .input(McpSchemaGenerator.forInput(McpAddTaskRelationInput.class))
                .output(McpSchemaGenerator.single("relation", "The created relation.", McpTaskRelationView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpAddTaskRelationInput input = args.bind(McpAddTaskRelationInput.class);
        TaskRelationInitializeRequest request = new TaskRelationInitializeRequest();
        request.setTaskId(input.getTaskId());
        request.setRelatedTaskId(input.getRelatedTaskId());
        request.setRelation(input.getRelationType());
        TaskRelationDto relation = taskRelationManager.initializeTaskRelation(request).getTaskRelationDto();
        return McpToolResult.of(McpTaskRelationEnvelopeView.of(mcpViewConverter.taskRelation(relation)));
    }
}
