package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpBoardEnvelopeView;
import co.jinear.core.model.mcp.view.McpBoardView;
import co.jinear.core.model.request.task.TaskBoardInitializeRequest;
import co.jinear.core.model.response.task.TaskBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpCreateTaskBoardInput;

@Service
@RequiredArgsConstructor
public class CreateTaskBoardTool implements McpTool {

    private final TaskBoardManager taskBoardManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("create_task_board")
                .title("Create a board")
                .description("Creates an empty board in a team. Add tasks to it afterwards with add_task_to_board.")
                .input(McpSchemaGenerator.forInput(McpCreateTaskBoardInput.class))
                .output(McpSchemaGenerator.single("board", "The created board.", McpBoardView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpCreateTaskBoardInput input = args.bind(McpCreateTaskBoardInput.class);
        TaskBoardInitializeRequest request = new TaskBoardInitializeRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setTeamId(input.getTeamId());
        request.setTitle(input.getTitle());
        request.setDueDate(input.getDueDate());
        context.setWorkspaceId(input.getWorkspaceId());
        TaskBoardResponse response = taskBoardManager.initializeTaskBoard(request);
        return McpToolResult.of(McpBoardEnvelopeView.of(mcpViewConverter.board(response.getTaskBoardDto())));
    }
}
