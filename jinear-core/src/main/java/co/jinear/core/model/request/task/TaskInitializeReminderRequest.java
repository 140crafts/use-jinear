package co.jinear.core.model.request.task;

import co.jinear.core.model.enumtype.reminder.RepeatType;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class TaskInitializeReminderRequest {

    @Nullable
    private Boolean beforeAssignedDate;

    @Nullable
    private Boolean beforeDueDate;

    @Nullable
    @Future(message = "task.reminder.initialize.date-not-future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime specificRemindDate;

    @Nullable
    @Future(message = "task.reminder.initialize.date-not-future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime specificRemindRepeatEnd;

    @Nullable
    private RepeatType specificRemindDateRepeatType;
}
