package co.jinear.core.model.vo.reminder;

import co.jinear.core.model.enumtype.reminder.ReminderType;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class ReminderInitializeVo {

    private String ownerId;
    private String relatedObjectId;
    private ReminderType type;
    private RepeatType repeatType;
    private ZonedDateTime repeatStart;
    private ZonedDateTime repeatEnd;
    private ZonedDateTime initialRemindDate;
}
