package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskCommentManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpCommentView;
import co.jinear.core.model.mcp.view.McpPageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpListTaskCommentsInput;

@Service
@RequiredArgsConstructor
public class ListTaskCommentsTool implements McpTool {

    private final TaskCommentManager taskCommentManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_task_comments")
                .title("List a task's comments")
                .description("Reads the discussion on a task, newest first. "
                             + "Use it to catch up on what has already been said before answering or adding a comment.")
                .input(McpSchemaGenerator.forInput(McpListTaskCommentsInput.class))
                .output(McpSchemaGenerator.page(McpCommentView.class, "Comments on this task."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTaskCommentsInput input = args.bind(McpListTaskCommentsInput.class);
        PageDto<CommentDto> page = taskCommentManager
                .retrieveTaskComments(input.getTaskId(), args.page())
                .getCommentsPage();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::comment));
    }
}
