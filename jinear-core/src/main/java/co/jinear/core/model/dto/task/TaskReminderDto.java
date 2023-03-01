package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskReminderDto extends BaseDto {
    @NotNull
    private String taskReminderId;
    @NotNull
    private String taskId;
    @NotNull
    private String reminderId;
    @NotNull
    private TaskReminderType taskReminderType;
    @NotNull
    private ReminderDto reminder;
}
