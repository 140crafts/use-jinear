package co.jinear.core.model.request.reminder;

import co.jinear.core.model.enumtype.reminder.RepeatType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
public class TaskReminderInitializeRequest extends BaseRequest {

    @Nullable
    private Boolean beforeAssignedDate;
    @Nullable
    private Boolean beforeDueDate;
    @Nullable
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime specificRemindDate;
    @Nullable
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime specificRemindRepeatStart;
    @Nullable
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime specificRemindRepeatEnd;
    @Nullable
    private RepeatType specificRemindDateRepeatType;
}
