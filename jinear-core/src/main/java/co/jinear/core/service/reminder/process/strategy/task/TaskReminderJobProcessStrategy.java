package co.jinear.core.service.reminder.process.strategy.task;

import co.jinear.core.converter.reminder.ReminderJobConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.enumtype.reminder.ReminderType;
import co.jinear.core.model.vo.reminder.InitializeReminderJobVo;
import co.jinear.core.service.reminder.job.ReminderJobDateCalculatorService;
import co.jinear.core.service.reminder.job.ReminderJobOperationService;
import co.jinear.core.service.reminder.job.ReminderJobRetrieveService;
import co.jinear.core.service.reminder.process.strategy.ReminderJobProcessStrategy;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.reminder.TaskReminderOperationService;
import co.jinear.core.service.task.reminder.TaskReminderRetrieveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.reminder.ReminderJobStatus.COMPLETED;
import static co.jinear.core.model.enumtype.reminder.ReminderJobStatus.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderJobProcessStrategy implements ReminderJobProcessStrategy {

    private final TaskRetrieveService taskRetrieveService;
    private final ReminderJobRetrieveService reminderJobRetrieveService;
    private final ReminderJobOperationService reminderJobOperationService;
    private final ReminderJobDateCalculatorService reminderJobDateCalculatorService;
    private final ReminderJobConverter reminderJobConverter;
    private final TaskReminderRetrieveService taskReminderRetrieveService;
    private final TaskReminderOperationService taskReminderOperationService;
    private final TaskReminderAccountReachOutService taskReminderAccountReachOutService;

    @Override
    @Transactional
    public void process(ReminderJobDto reminderJobDto) {
        log.info("Task reminder job process has started. reminderJobDto: {}", reminderJobDto);
        TaskDto taskDto = retrieveTask(reminderJobDto);
        TaskReminderDto taskReminderDto = taskReminderRetrieveService.retrieveByTaskIdAndReminderId(taskDto.getTaskId(), reminderJobDto.getReminderId());
        taskReminderAccountReachOutService.notify(taskDto, taskReminderDto, reminderJobDto);
        reminderJobOperationService.updateReminderJobStatus(reminderJobDto.getReminderJobId(), COMPLETED);
        calculateNextDateAndInitializeNextReminderJob(reminderJobDto, taskReminderDto);
    }

    @Override
    public ReminderType getType() {
        return ReminderType.TASK;
    }

    private TaskDto retrieveTask(ReminderJobDto reminderJobDto) {
        return Optional.of(reminderJobDto)
                .map(ReminderJobDto::getReminder)
                .map(ReminderDto::getRelatedObjectId)
                .map(taskRetrieveService::retrieve)
                .orElseThrow(BusinessException::new);
    }

    private void calculateNextDateAndInitializeNextReminderJob(ReminderJobDto reminderJobDto, TaskReminderDto taskReminderDto) {
        reminderJobDateCalculatorService.calculateNextDate(reminderJobDto)
                .ifPresentOrElse(
                        nextDate -> initializeNextReminderJob(nextDate, reminderJobDto, taskReminderDto),
                        () -> {
                            log.info("Next date is null. Won't initialize next reminder job and set reminder as passive");
                            passivizeReminderAndAllActiveJobs(taskReminderDto);
                        });
    }

    private void initializeNextReminderJob(ZonedDateTime nextDate, ReminderJobDto reminderJobDto, TaskReminderDto taskReminderDto) {
        final String reminderId = reminderJobDto.getReminderId();
        ZonedDateTime repeatEnd = reminderJobDto.getReminder().getRepeatEnd();
        if (Objects.nonNull(repeatEnd) && nextDate.isAfter(repeatEnd)) {
            log.info("Next date is after repeat end. Won't initialize next reminder job and set reminder as passive.");
            passivizeReminderAndAllActiveJobs(taskReminderDto);
            return;
        }
        if (reminderJobRetrieveService.checkIfAnyReminderJobExistsForReminderAtDate(reminderId, nextDate)) {
            log.info("There is an existing reminder. Not creating new one. reminderId: {}, nextDate: {}", reminderId, nextDate);
            return;
        }
        InitializeReminderJobVo nextInitializeReminderJobVo = reminderJobConverter.map(nextDate, reminderId, PENDING);
        reminderJobOperationService.initializeReminderJob(nextInitializeReminderJobVo);
        log.info("Next reminder job has initialized. nextDate: {}", nextDate);
    }

    private void passivizeReminderAndAllActiveJobs(TaskReminderDto taskReminderDto) {
        taskReminderOperationService.passivizeTaskReminder(taskReminderDto.getTaskReminderId());
    }
}
