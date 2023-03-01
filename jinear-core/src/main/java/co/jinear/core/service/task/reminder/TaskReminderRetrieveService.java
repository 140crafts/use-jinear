package co.jinear.core.service.task.reminder;

import co.jinear.core.converter.task.TaskReminderConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.entity.task.TaskReminder;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.repository.task.TaskReminderRepository;
import co.jinear.core.service.reminder.job.ReminderJobRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderRetrieveService {

    private final TaskReminderRepository taskReminderRepository;
    private final TaskReminderConverter taskReminderConverter;
    private final ReminderJobRetrieveService reminderJobRetrieveService;

    public TaskReminderDto retrieve(String taskReminderId) {
        log.info("Retrieve task reminder has started. taskReminderId: {}", taskReminderId);
        return taskReminderRepository.findByTaskReminderIdAndPassiveIdIsNull(taskReminderId)
                .map(taskReminderConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public TaskReminder retrieveEntity(String taskReminderId) {
        log.info("Retrieve task reminder entity has started. taskReminderId: {}", taskReminderId);
        return taskReminderRepository.findByTaskReminderIdAndPassiveIdIsNull(taskReminderId)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<TaskReminderDto> retrieveRelated(String taskId, TaskReminderType taskReminderType) {
        log.info("Retrieve related task reminder exists has started. taskId: {}, taskReminderType:{}", taskId, taskReminderType);
        return taskReminderRepository.findFirstByTaskIdAndTaskReminderTypeAndPassiveIdIsNull(taskId, taskReminderType)
                .map(taskReminderConverter::map);
    }

    public boolean hasAnyRelated(String taskId, TaskReminderType taskReminderType) {
        log.info("Check any related task reminder exists has started. taskId: {}, taskReminderType:{}", taskId, taskReminderType);
        boolean relatedTaskReminderExists = retrieveRelated(taskId, taskReminderType).isPresent();
        log.info("Related task reminder exists: {}", relatedTaskReminderExists);
        return relatedTaskReminderExists;
    }

    public ReminderJobDto retrieveNextPendingReminderJob(String taskReminderId) {
        log.info("Retrieve next reminder job has started. taskReminderId: {}", taskReminderId);
        TaskReminderDto taskReminderDto = retrieve(taskReminderId);
        return reminderJobRetrieveService.findFirstReminderJob(taskReminderDto.getReminderId(), ReminderJobStatus.PENDING);
    }

    public TaskReminderDto retrieveByTaskIdAndReminderId(String taskId, String reminderId) {
        log.info("Retrieve task reminder by task id and reminder id has started. taskId: {}, reminderId: {}", taskId, reminderId);
        return taskReminderRepository.findByTaskIdAndReminderIdAndPassiveIdIsNull(taskId, reminderId)
                .map(taskReminderConverter::map)
                .orElseThrow(NotFoundException::new);
    }
}
