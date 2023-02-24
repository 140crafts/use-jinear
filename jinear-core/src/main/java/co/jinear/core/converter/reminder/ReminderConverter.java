package co.jinear.core.converter.reminder;

import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.entity.reminder.Reminder;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import co.jinear.core.model.vo.reminder.ReminderInitializeVo;
import co.jinear.core.model.vo.task.InitializeTaskRemindersVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReminderConverter {

    Reminder map(ReminderInitializeVo reminderInitializeVo);

    ReminderDto map(Reminder reminder);

    InitializeTaskRemindersVo map(TaskReminderInitializeRequest reminderInitializeRequest, String taskId, String ownerId);
}
