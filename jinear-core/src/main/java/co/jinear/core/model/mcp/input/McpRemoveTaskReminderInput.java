package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpRemoveTaskReminderInput {

    @NotBlank
    @McpField(value = "Reminder id, from list_task_reminders.", hint = "Call list_task_reminders to find it.")
    private String taskReminderId;
}
