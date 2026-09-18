package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskCommentManager;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpCommentAcknowledgementView;
import co.jinear.core.model.request.task.InitializeTaskCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpAddTaskCommentInput;

@Service
@RequiredArgsConstructor
public class AddTaskCommentTool implements McpTool {

    private final TaskCommentManager taskCommentManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("add_task_comment")
                .title("Comment on a task")
                .description("Adds a comment to a task, attributed to the signed in account. "
                             + "Use it to record a decision, an update or an answer where the work lives. "
                             + "Returns the new comment's id.")
                .input(McpSchemaGenerator.forInput(McpAddTaskCommentInput.class))
                .output(McpSchemaGenerator.forType(McpCommentAcknowledgementView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpAddTaskCommentInput input = args.bind(McpAddTaskCommentInput.class);
        InitializeTaskCommentRequest request = new InitializeTaskCommentRequest();
        request.setTaskId(input.getTaskId());
        request.setComment(input.getComment());
        request.setQuoteCommentId(input.getQuoteCommentId());
        CommentDto comment = taskCommentManager.initializeComment(request).getCommentDto();
        return McpToolResult.of(McpCommentAcknowledgementView.of(input.getTaskId(), comment.getCommentId()));
    }
}
