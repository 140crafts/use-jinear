package co.jinear.core.service.task.reminder;

import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.service.reminder.job.ReminderJobOperationService;
import co.jinear.core.service.reminder.job.ReminderJobRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderDateUpdateService {

    private final TaskReminderRetrieveService taskReminderRetrieveService;
    private final ReminderJobRetrieveService reminderJobRetrieveService;
    private final ReminderJobOperationService reminderJobOperationService;

    public void updateTaskReminderWithTypeIfExists(String taskId, TaskReminderType taskReminderType, ZonedDateTime date) {
        log.info("Update task assigned date reminder if exists has started. taskId: {}, taskReminderType: {}, date: {}", taskId, taskReminderType, date);
        taskReminderRetrieveService.retrieveRelated(taskId, taskReminderType)
                .map(TaskReminderDto::getTaskReminderId)
                .ifPresent(taskReminderId -> updateTaskReminderDate(taskReminderId, date));
    }

    private void updateTaskReminderDate(String taskReminderId, ZonedDateTime date) {
        log.info("Update task reminder date has started. taskReminderId: {}, date: {}", taskReminderId, date);
        TaskReminderDto taskReminderDto = taskReminderRetrieveService.retrieve(taskReminderId);
        ReminderJobDto reminderJobDto = reminderJobRetrieveService.findFirstReminderJob(taskReminderDto.getReminderId(), ReminderJobStatus.PENDING);
        reminderJobOperationService.updateDate(reminderJobDto.getReminderJobId(), date);
        log.info("Update task reminder date has completed.");
    }
}
