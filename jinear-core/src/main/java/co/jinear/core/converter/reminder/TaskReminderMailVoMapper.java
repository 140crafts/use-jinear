package co.jinear.core.converter.reminder;

import co.jinear.core.converter.task.TaskTagConverter;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.util.ZonedDateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskReminderMailVoMapper {

    private final TaskTagConverter taskTagConverter;

    public TaskReminderMailVo map(String email, LocaleType preferredLocale, String timeZone, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        TaskReminderMailVo taskReminderMailVo = new TaskReminderMailVo();

        taskReminderMailVo.setEmail(email);
        taskReminderMailVo.setPreferredLocale(preferredLocale);
        taskReminderMailVo.setTaskId(taskDto.getTaskId());
        taskReminderMailVo.setTaskTitle(taskDto.getTitle());
        taskReminderMailVo.setTaskReminderType(taskReminderDto.getTaskReminderType());

        mapWorkspaceName(taskDto, taskReminderMailVo);
        mapLocaleDate(timeZone, reminderJobDto, taskReminderMailVo);
        mapTaskDetail(taskDto, taskReminderMailVo);
        mapTaskTag(taskDto, taskReminderMailVo);

        return taskReminderMailVo;
    }

    private void mapWorkspaceName(TaskDto taskDto, TaskReminderMailVo taskReminderMailVo) {
        Optional.of(taskDto)
                .map(TaskDto::getWorkspace)
                .map(WorkspaceDto::getUsername)
                .ifPresent(taskReminderMailVo::setWorkspaceName);
    }

    private void mapLocaleDate(String timeZone, ReminderJobDto reminderJobDto, TaskReminderMailVo taskReminderMailVo) {
        Optional.of(timeZone)
                .map(ZoneId::of)
                .map(userZoneId -> reminderJobDto.getDate().withZoneSameInstant(userZoneId))
                .map(ZonedDateHelper::formatWithDateTimeFormat1)
                .ifPresent(taskReminderMailVo::setAccountLocaleDate);
    }

    private void mapTaskDetail(TaskDto taskDto, TaskReminderMailVo taskReminderMailVo) {
        String taskDetail = Optional.of(taskDto)
                .map(TaskDto::getDescription)
                .map(RichTextDto::getValue)
                .orElse(NormalizeHelper.EMPTY_STRING);
        taskReminderMailVo.setTaskDetail(taskDetail);
    }

    private void mapTaskTag(TaskDto taskDto, TaskReminderMailVo taskReminderMailVo) {
        String taskTag = taskTagConverter.retrieveTaskTag(taskDto);
        taskReminderMailVo.setTaskTag(taskTag);
    }
}
