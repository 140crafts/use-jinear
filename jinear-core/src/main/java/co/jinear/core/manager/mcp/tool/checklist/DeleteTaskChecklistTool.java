package co.jinear.core.manager.mcp.tool.checklist;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpDeleteTaskChecklistInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpChecklistAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskChecklistTool implements McpTool {

    private final TaskChecklistManager taskChecklistManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("delete_task_checklist")
                .title("Delete a checklist")
                .description("Deletes a checklist and all of its items from a task.")
                .input(McpSchemaGenerator.forInput(McpDeleteTaskChecklistInput.class))
                .output(McpSchemaGenerator.acknowledgement("checklistId", "The checklist that was deleted."))
                .destructive()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpDeleteTaskChecklistInput input = args.bind(McpDeleteTaskChecklistInput.class);
        taskChecklistManager.passivizeChecklist(input.getChecklistId());
        return McpToolResult.of(McpChecklistAcknowledgementView.of(input.getChecklistId()));
    }
}
