package co.jinear.core.converter.reminder;

import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.entity.reminder.Reminder;
import co.jinear.core.model.entity.reminder.ReminderJob;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.model.vo.reminder.InitializeReminderJobVo;
import co.jinear.core.model.vo.reminder.ReminderInitializeVo;
import org.mapstruct.*;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface ReminderJobConverter {

    ReminderJob map(InitializeReminderJobVo initializeReminderJobVo);

    ReminderJobDto map(ReminderJob reminderJob);

    InitializeReminderJobVo map(ZonedDateTime date,String reminderId);

    @Mapping(source = "reminder.reminderId", target = "reminderId")
    @Mapping(source = "reminderInitializeVo.initialRemindDate", target = "date")
    @BeanMapping(qualifiedByName = "initializeReminderJobVoMapper")
    InitializeReminderJobVo map(Reminder reminder, ReminderInitializeVo reminderInitializeVo);

    @AfterMapping
    @Named("initializeReminderJobVoMapper")
    default void afterMap(@MappingTarget InitializeReminderJobVo initializeReminderJobVo) {
        initializeReminderJobVo.setReminderJobStatus(ReminderJobStatus.PENDING);
    }
}
