package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpCommentView implements McpToolPayload {

    @McpField("Comment id.")
    private String commentId;

    @McpField("Task this comment belongs to.")
    private String taskId;

    @McpField("Account id of the author.")
    private String authorAccountId;

    @McpField("Author handle.")
    private String authorUsername;

    @McpField("Comment body as HTML.")
    private String body;

    @McpField("ISO 8601 instant the comment was posted.")
    private String createdAt;
}
