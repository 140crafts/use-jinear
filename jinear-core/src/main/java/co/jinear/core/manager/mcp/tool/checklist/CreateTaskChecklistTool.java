package co.jinear.core.manager.mcp.tool.checklist;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpCreateTaskChecklistInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpChecklistEnvelopeView;
import co.jinear.core.model.mcp.view.McpChecklistView;
import co.jinear.core.model.request.task.InitializeChecklistRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskChecklistTool implements McpTool {

    private final TaskChecklistManager taskChecklistManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("create_task_checklist")
                .title("Add a checklist to a task")
                .description("Adds a checklist to a task, optionally with its first item. A task can hold more than one checklist. "
                             + "Add more items with add_checklist_item. Returns the checklist with its id.")
                .input(McpSchemaGenerator.forInput(McpCreateTaskChecklistInput.class))
                .output(McpSchemaGenerator.single("checklist", "The created checklist.", McpChecklistView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpCreateTaskChecklistInput input = args.bind(McpCreateTaskChecklistInput.class);
        InitializeChecklistRequest request = new InitializeChecklistRequest();
        request.setTaskId(input.getTaskId());
        request.setTitle(input.getTitle());
        request.setInitialItemLabel(input.getFirstItemLabel());
        ChecklistDto checklist = taskChecklistManager.initializeChecklist(request).getChecklistDto();
        return McpToolResult.of(McpChecklistEnvelopeView.of(mcpViewConverter.checklist(checklist)));
    }
}
