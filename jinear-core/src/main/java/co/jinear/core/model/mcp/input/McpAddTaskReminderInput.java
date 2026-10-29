package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.reminder.RepeatType;
import co.jinear.core.model.mcp.schema.McpField;
import co.jinear.core.model.mcp.schema.McpZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class McpAddTaskReminderInput {

    @NotBlank
    @McpField(McpInputDescriptions.TASK_ID)
    private String taskId;

    @McpField("Remind at the task's start date. The reminder moves when the start date changes. "
              + "Skipped when the task has no start date or already has a start date reminder.")
    private Boolean atStartDate;

    @McpField("Remind at the task's due date. The reminder moves when the due date changes. "
              + "Skipped when the task has no due date or already has a due date reminder.")
    private Boolean atDueDate;

    @Future
    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Instant for a reminder at a specific time. Must be in the future.")
    private ZonedDateTime remindAt;

    @McpField("How the remindAt reminder repeats. Defaults to NONE.")
    private RepeatType repeat;

    @Future
    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Last instant a repeating remindAt reminder may fire. Leave out for no end.")
    private ZonedDateTime repeatUntil;
}
