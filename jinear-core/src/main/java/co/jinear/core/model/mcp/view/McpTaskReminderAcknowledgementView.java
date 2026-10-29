package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskReminderAcknowledgementView implements McpToolPayload {

    @McpField("Id of the removed reminder.")
    private String taskReminderId;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpTaskReminderAcknowledgementView of(String taskReminderId) {
        McpTaskReminderAcknowledgementView view = new McpTaskReminderAcknowledgementView();
        view.setTaskReminderId(taskReminderId);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
