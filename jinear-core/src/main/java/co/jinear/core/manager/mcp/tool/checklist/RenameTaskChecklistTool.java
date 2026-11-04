package co.jinear.core.manager.mcp.tool.checklist;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpRenameTaskChecklistInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpChecklistAcknowledgementView;
import co.jinear.core.model.request.task.UpdateChecklistTitleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RenameTaskChecklistTool implements McpTool {

    private final TaskChecklistManager taskChecklistManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("rename_task_checklist")
                .title("Rename a checklist")
                .description("Changes the title of a checklist on a task.")
                .input(McpSchemaGenerator.forInput(McpRenameTaskChecklistInput.class))
                .output(McpSchemaGenerator.acknowledgement("checklistId", "The checklist that was renamed."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpRenameTaskChecklistInput input = args.bind(McpRenameTaskChecklistInput.class);
        UpdateChecklistTitleRequest request = new UpdateChecklistTitleRequest();
        request.setTitle(input.getTitle());
        taskChecklistManager.updateChecklistLabel(input.getChecklistId(), request);
        return McpToolResult.of(McpChecklistAcknowledgementView.of(input.getChecklistId()));
    }
}
