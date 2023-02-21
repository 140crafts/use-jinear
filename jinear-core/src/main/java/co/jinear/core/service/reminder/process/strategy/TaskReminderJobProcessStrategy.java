package co.jinear.core.service.reminder.process.strategy;

import co.jinear.core.converter.reminder.ReminderJobConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.reminder.ReminderType;
import co.jinear.core.model.vo.reminder.InitializeReminderJobVo;
import co.jinear.core.service.reminder.job.ReminderJobDateCalculatorService;
import co.jinear.core.service.reminder.job.ReminderJobOperationService;
import co.jinear.core.service.task.TaskRetrieveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static co.jinear.core.model.enumtype.reminder.ReminderJobStatus.COMPLETED;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderJobProcessStrategy implements ReminderJobProcessStrategy {

    private final TaskRetrieveService taskRetrieveService;
    private final ReminderJobOperationService reminderJobOperationService;
    private final ReminderJobDateCalculatorService reminderJobDateCalculatorService;
    private final ReminderJobConverter reminderJobConverter;

    @Override
    @Transactional
    public void process(ReminderJobDto reminderJobDto) {
        log.info("Task reminder job process has started. reminderJobDto: {}", reminderJobDto);
        TaskDto taskDto = retrieveTask(reminderJobDto);
        log.info("[TODO] SEND MAIL taskDto: {}", taskDto);
        reminderJobOperationService.updateReminderJobStatus(reminderJobDto.getReminderJobId(), COMPLETED);
        initializeNextReminderJob(reminderJobDto);
    }

    private void initializeNextReminderJob(ReminderJobDto reminderJobDto) {
        reminderJobDateCalculatorService.calculateNextDate(reminderJobDto).ifPresent(nextDate -> {
            InitializeReminderJobVo nextInitializeReminderJobVo = reminderJobConverter.map(nextDate, reminderJobDto.getReminderId());
            reminderJobOperationService.initializeReminderJob(nextInitializeReminderJobVo);
        });
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
}
