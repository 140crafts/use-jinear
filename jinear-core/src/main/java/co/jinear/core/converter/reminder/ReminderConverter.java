package co.jinear.core.converter.reminder;

import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.entity.reminder.Reminder;
import co.jinear.core.model.vo.reminder.ReminderInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReminderConverter {

    Reminder map(ReminderInitializeVo reminderInitializeVo);

    ReminderDto map(Reminder reminder);
}
