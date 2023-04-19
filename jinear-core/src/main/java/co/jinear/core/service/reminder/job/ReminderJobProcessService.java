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

    public void processAllUpcomingJobs(ZonedDateTime beforeDate) {
        log.info("Process all upcoming jobs before date has started. beforeDate: {}", beforeDate);
        reminderJobListingService.retrieveAllByReminderJobStatusAndBeforeDate(ReminderJobStatus.PENDING, beforeDate)
                .stream()
                .forEach(this::process);
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
