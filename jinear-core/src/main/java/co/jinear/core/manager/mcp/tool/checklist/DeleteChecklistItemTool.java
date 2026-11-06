package co.jinear.core.manager.mcp.tool.checklist;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskChecklistItemManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpDeleteChecklistItemInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpChecklistItemAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteChecklistItemTool implements McpTool {

    private final TaskChecklistItemManager taskChecklistItemManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("delete_checklist_item")
                .title("Delete a checklist item")
                .description("Deletes one item from a checklist. The other items and the checklist stay as they are.")
                .input(McpSchemaGenerator.forInput(McpDeleteChecklistItemInput.class))
                .output(McpSchemaGenerator.acknowledgement("checklistItemId", "The item that was deleted."))
                .destructive()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpDeleteChecklistItemInput input = args.bind(McpDeleteChecklistItemInput.class);
        taskChecklistItemManager.passivizeChecklistItem(input.getChecklistItemId());
        return McpToolResult.of(McpChecklistItemAcknowledgementView.of(input.getChecklistItemId()));
    }
}
