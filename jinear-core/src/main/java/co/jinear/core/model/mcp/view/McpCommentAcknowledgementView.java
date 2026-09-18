package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpCommentAcknowledgementView implements McpToolPayload {

    @McpField("The task the comment was added to.")
    private String taskId;

    @McpField("Id of the new comment. Pass it as quoteCommentId to reply to it.")
    private String commentId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpCommentAcknowledgementView of(String taskId, String commentId) {
        McpCommentAcknowledgementView view = new McpCommentAcknowledgementView();
        view.setTaskId(taskId);
        view.setCommentId(commentId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
