package co.jinear.core.service.reminder.process.strategy;

import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.enumtype.reminder.ReminderType;

public interface ReminderJobProcessStrategy {

    void process(ReminderJobDto reminderJobDto);

    ReminderType getType();
}
