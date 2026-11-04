package co.jinear.core.manager.mcp.tool.checklist;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskChecklistItemManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpUpdateChecklistItemInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpChecklistItemAcknowledgementView;
import co.jinear.core.model.request.task.ChecklistItemLabelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateChecklistItemTool implements McpTool {

    private final TaskChecklistItemManager taskChecklistItemManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("update_checklist_item")
                .title("Update a checklist item")
                .description("Checks or unchecks a checklist item, changes its text, or both. "
                             + "Supply only the fields to change.")
                .input(McpSchemaGenerator.forInput(McpUpdateChecklistItemInput.class))
                .output(McpSchemaGenerator.acknowledgement("checklistItemId", "The item that was updated."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpUpdateChecklistItemInput input = args.bind(McpUpdateChecklistItemInput.class);
        boolean labelChanged = applyLabel(input);
        boolean checkedChanged = applyChecked(input);
        if (!(labelChanged || checkedChanged)) {
            return McpToolResult.error("mcp.error.checklist-item.nothing-to-update");
        }
        return McpToolResult.of(McpChecklistItemAcknowledgementView.of(input.getChecklistItemId()));
    }

    private boolean applyLabel(McpUpdateChecklistItemInput input) {
        if (Objects.isNull(input.getLabel())) {
            return false;
        }
        ChecklistItemLabelRequest request = new ChecklistItemLabelRequest();
        request.setLabel(input.getLabel());
        taskChecklistItemManager.updateLabel(input.getChecklistItemId(), request);
        return true;
    }

    private boolean applyChecked(McpUpdateChecklistItemInput input) {
        if (Objects.isNull(input.getChecked())) {
            return false;
        }
        taskChecklistItemManager.updateCheckedStatus(input.getChecklistItemId(), input.getChecked());
        return true;
    }
}
