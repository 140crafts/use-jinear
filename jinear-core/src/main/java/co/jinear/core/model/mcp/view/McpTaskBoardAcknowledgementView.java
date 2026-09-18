package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskBoardAcknowledgementView implements McpToolPayload {

    @McpField("The board the task was added to.")
    private String taskBoardId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpTaskBoardAcknowledgementView of(String taskBoardId) {
        McpTaskBoardAcknowledgementView view = new McpTaskBoardAcknowledgementView();
        view.setTaskBoardId(taskBoardId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
