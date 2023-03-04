package co.jinear.core.converter.reminder;

import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.util.ZonedDateHelper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.ZoneId;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TaskReminderMailVoConverter {

    @Mapping(source = "plainAccountProfileDto.localeType", target = "preferredLocale")
    @Mapping(source = "taskDto.taskId", target = "taskId")
    @Mapping(source = "taskDto.title", target = "taskTitle")
    @Mapping(source = "taskDto.description.value", target = "taskDetail")
    @Mapping(source = "taskReminderDto.taskReminderType", target = "taskReminderType")
    @Mapping(source = "taskDto.workspace.username", target = "workspaceName")
    TaskReminderMailVo map(PlainAccountProfileDto plainAccountProfileDto, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto);

    @AfterMapping
    default void afterMap(@MappingTarget TaskReminderMailVo taskReminderMailVo, TaskDto task, PlainAccountProfileDto plainAccountProfileDto, ReminderJobDto reminderJobDto) {
        //TODO cgds-73
        Optional.of(plainAccountProfileDto)
                .map(PlainAccountProfileDto::getTimeZone)
                .map(ZoneId::of)
                .map(userZoneId -> reminderJobDto.getDate().withZoneSameInstant(userZoneId))
                .map(ZonedDateHelper::formatWithDateTimeFormat1)
                .ifPresent(taskReminderMailVo::setAccountLocaleDate);

        String taskDetail = Optional.of(task)
                .map(TaskDto::getDescription)
                .map(RichTextDto::getValue)
                .orElse(NormalizeHelper.EMPTY_STRING);
        taskReminderMailVo.setTaskDetail(taskDetail);

        StringBuilder sb = new StringBuilder();
        Optional.of(task)
                .map(TaskDto::getTeam)
                .map(TeamDto::getTag)
                .ifPresent(teamTag -> sb.append(teamTag).append(NormalizeHelper.HYPHEN));
        Optional.of(task)
                .map(TaskDto::getTeamTagNo)
                .map(Object::toString)
                .ifPresent(sb::append);
        taskReminderMailVo.setTaskTag(sb.toString());
    }
}
