package co.jinear.core.manager.mcp.tool.checklist;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskChecklistItemManager;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpAddChecklistItemInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpChecklistItemEnvelopeView;
import co.jinear.core.model.mcp.view.McpChecklistItemView;
import co.jinear.core.model.request.task.ChecklistItemLabelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddChecklistItemTool implements McpTool {

    private final TaskChecklistItemManager taskChecklistItemManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("add_checklist_item")
                .title("Add an item to a checklist")
                .description("Adds an unchecked item to the end of a checklist. Returns the item with its id.")
                .input(McpSchemaGenerator.forInput(McpAddChecklistItemInput.class))
                .output(McpSchemaGenerator.single("checklistItem", "The created item.", McpChecklistItemView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpAddChecklistItemInput input = args.bind(McpAddChecklistItemInput.class);
        ChecklistItemLabelRequest request = new ChecklistItemLabelRequest();
        request.setChecklistId(input.getChecklistId());
        request.setLabel(input.getLabel());
        ChecklistItemDto checklistItem = taskChecklistItemManager.initializeChecklistItem(request).getChecklistItemDto();
        return McpToolResult.of(McpChecklistItemEnvelopeView.of(mcpViewConverter.checklistItem(checklistItem)));
    }
}
