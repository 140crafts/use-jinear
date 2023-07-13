package co.jinear.core.service.reminder.job;

import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.service.reminder.process.strategy.ReminderJobProcessStrategy;
import co.jinear.core.service.reminder.process.strategy.ReminderJobProcessStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderJobProcessService {

    private final ReminderJobListingService reminderJobListingService;
    private final ReminderJobProcessStrategyFactory reminderJobProcessStrategyFactory;
    private final ReminderJobProcessLockService reminderJobProcessLockService;

    public void processAllUpcomingJobs(ZonedDateTime beforeDate) {
        reminderJobProcessLockService.lockReminderJobProcessing();
        log.info("Process all upcoming jobs before date has started. beforeDate: {}", beforeDate);
        try {
            reminderJobListingService.retrieveAllByReminderJobStatusAndBeforeDate(ReminderJobStatus.PENDING, beforeDate)
                    .forEach(this::process);
        } finally {
            reminderJobProcessLockService.unlockReminderJobProcessing();
        }
    }

    private void process(ReminderJobDto reminderJobDto) {
        log.info("Process reminder job has started. reminderJobDto: {}", reminderJobDto);
        try {
            ReminderJobProcessStrategy reminderJobProcessStrategy = reminderJobProcessStrategyFactory.getStrategy(reminderJobDto.getReminder().getType());
            reminderJobProcessStrategy.process(reminderJobDto);
        } catch (Exception e) {
            log.error("Process reminder job has failed.", e);
        }
    }
}
