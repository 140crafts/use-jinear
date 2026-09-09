package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpAddTaskCommentInput {

    @NotBlank
    @McpField(McpInputDescriptions.TASK_ID)
    private String taskId;

    @NotBlank
    @McpField("Comment body. Plain text or simple HTML.")
    private String comment;

    @McpField("Comment id being replied to, if this is a reply.")
    private String quoteCommentId;
}
