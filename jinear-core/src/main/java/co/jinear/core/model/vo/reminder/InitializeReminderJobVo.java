package co.jinear.core.model.vo.reminder;

import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InitializeReminderJobVo {

    private String reminderId;
    private ZonedDateTime date;
    private ReminderJobStatus reminderJobStatus;
}
