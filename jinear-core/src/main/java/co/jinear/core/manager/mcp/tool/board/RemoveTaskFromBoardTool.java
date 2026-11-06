package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpRemoveTaskFromBoardInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskBoardEntryAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveTaskFromBoardTool implements McpTool {

    private final TaskBoardEntryManager taskBoardEntryManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("remove_task_from_board")
                .title("Remove a task from a board")
                .description("Takes a task off a board. The task itself is not deleted and keeps its team, status and assignee.")
                .input(McpSchemaGenerator.forInput(McpRemoveTaskFromBoardInput.class))
                .output(McpSchemaGenerator.acknowledgement("taskBoardEntryId", "The board entry that was removed."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpRemoveTaskFromBoardInput input = args.bind(McpRemoveTaskFromBoardInput.class);
        taskBoardEntryManager.deleteTaskBoardEntry(input.getTaskBoardEntryId());
        return McpToolResult.of(McpTaskBoardEntryAcknowledgementView.of(input.getTaskBoardEntryId()));
    }
}
