package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskReminderView implements McpToolPayload {

    @McpField("Reminder id. Pass it to remove_task_reminder.")
    private String taskReminderId;

    @McpField("Task this reminder belongs to.")
    private String taskId;

    @McpField("START_DATE and DUE_DATE follow the task's dates. SPECIFIC_DATE fires at a fixed instant.")
    private String type;

    @McpField("How the reminder repeats. NONE for a one time reminder.")
    private String repeat;

    @McpField("Last instant a repeating reminder may fire, or null for no end.")
    private String repeatUntil;

    @McpField("Next instant the reminder fires, or null when it will not fire again.")
    private String nextRemindAt;

    @McpField("Account id of the person who added the reminder.")
    private String createdByAccountId;
}
