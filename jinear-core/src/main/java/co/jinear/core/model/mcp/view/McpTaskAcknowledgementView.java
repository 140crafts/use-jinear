package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskAcknowledgementView implements McpToolPayload {

    @McpField("The task that was updated.")
    private String taskId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpTaskAcknowledgementView of(String taskId) {
        McpTaskAcknowledgementView view = new McpTaskAcknowledgementView();
        view.setTaskId(taskId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
