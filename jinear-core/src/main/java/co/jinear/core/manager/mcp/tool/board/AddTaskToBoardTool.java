package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskBoardAcknowledgementView;
import co.jinear.core.model.request.task.TaskBoardEntryInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpAddTaskToBoardInput;

@Service
@RequiredArgsConstructor
public class AddTaskToBoardTool implements McpTool {

    private final TaskBoardEntryManager taskBoardEntryManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("add_task_to_board")
                .title("Add a task to a board")
                .description("Puts an existing task onto a board. The task keeps its team, status and assignee; "
                             + "only its board membership changes.")
                .input(McpSchemaGenerator.forInput(McpAddTaskToBoardInput.class))
                .output(McpSchemaGenerator.acknowledgement("taskBoardId", "The board the task was added to."))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpAddTaskToBoardInput input = args.bind(McpAddTaskToBoardInput.class);
        TaskBoardEntryInitializeRequest request = new TaskBoardEntryInitializeRequest();
        request.setTaskBoardId(input.getTaskBoardId());
        request.setTaskId(input.getTaskId());
        taskBoardEntryManager.initializeTaskBoardEntry(request);
        return McpToolResult.of(McpTaskBoardAcknowledgementView.of(input.getTaskBoardId()));
    }
}
