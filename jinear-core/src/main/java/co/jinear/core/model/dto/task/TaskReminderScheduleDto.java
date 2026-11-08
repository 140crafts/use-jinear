package co.jinear.core.model.dto.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class TaskReminderScheduleDto {

    private TaskReminderDto taskReminder;
    @Nullable
    private ZonedDateTime nextRemindDate;
}
