package co.jinear.core.model.vo.task;

import co.jinear.core.model.enumtype.reminder.RepeatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InitializeTaskRemindersVo {

    private String ownerId;
    private String taskId;
    private Boolean beforeAssignedDate;
    private Boolean beforeDueDate;
    private ZonedDateTime specificRemindDate;
    private RepeatType specificRemindDateRepeatType;
    private ZonedDateTime specificRemindRepeatStart;
    private ZonedDateTime specificRemindRepeatEnd;
}
