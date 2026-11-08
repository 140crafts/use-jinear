package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpMoveTaskOnBoardInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskBoardEntryAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveTaskOnBoardTool implements McpTool {

    private final TaskBoardEntryManager taskBoardEntryManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("move_task_on_board")
                .title("Move a task on a board")
                .description("Moves a task to a new position on a board. The tasks between the old and the new position shift by one. "
                             + "The board must be OPEN.")
                .input(McpSchemaGenerator.forInput(McpMoveTaskOnBoardInput.class))
                .output(McpSchemaGenerator.acknowledgement("taskBoardEntryId", "The board entry that was moved."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpMoveTaskOnBoardInput input = args.bind(McpMoveTaskOnBoardInput.class);
        taskBoardEntryManager.changeOrder(input.getTaskBoardEntryId(), input.getNewOrder());
        return McpToolResult.of(McpTaskBoardEntryAcknowledgementView.of(input.getTaskBoardEntryId()));
    }
}
