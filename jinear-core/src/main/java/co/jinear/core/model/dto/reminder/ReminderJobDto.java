package co.jinear.core.model.dto.reminder;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class ReminderJobDto extends BaseDto {

    private String reminderJobId;
    private String reminderId;
    private ZonedDateTime date;
    private ReminderJobStatus reminderJobStatus;
    private ReminderDto reminder;
}
