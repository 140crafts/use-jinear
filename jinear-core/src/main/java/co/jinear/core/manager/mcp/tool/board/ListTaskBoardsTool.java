package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpBoardView;
import co.jinear.core.model.mcp.view.McpPageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpListTaskBoardsInput;

@Service
@RequiredArgsConstructor
public class ListTaskBoardsTool implements McpTool {

    private final TaskBoardListingManager taskBoardListingManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_task_boards")
                .title("List a team's boards")
                .description("Lists the boards belonging to a team, newest first. "
                             + "A board groups tasks for a sprint, a release or any other batch of work.")
                .input(McpSchemaGenerator.forInput(McpListTaskBoardsInput.class))
                .output(McpSchemaGenerator.page(McpBoardView.class, "Boards in this team."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTaskBoardsInput input = args.bind(McpListTaskBoardsInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<TaskBoardDto> page = taskBoardListingManager
                .retrieveAllByTeam(input.getWorkspaceId(), input.getTeamId(), args.page())
                .getTaskListDetailedDtoPageDto();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::board));
    }
}
