package co.jinear.core.converter.task;

import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.entity.reminder.ReminderJob;
import co.jinear.core.model.entity.task.TaskReminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskReminderConverter {

    TaskReminderDto map(TaskReminder taskReminder);

    @Mapping(target = "reminder", ignore = true)
    ReminderJobDto map(ReminderJob reminderJob);
}
