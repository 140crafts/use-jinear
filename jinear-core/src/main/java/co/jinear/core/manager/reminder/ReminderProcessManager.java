package co.jinear.core.manager.reminder;

import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.reminder.job.ReminderJobProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static co.jinear.core.system.ReminderUtilities.REMINDER_INTERVAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderProcessManager {

    private final ReminderJobProcessService reminderJobProcessService;

    public BaseResponse remindUpcomingJobs() {
        ZonedDateTime future = ZonedDateTime.now().plusMinutes(REMINDER_INTERVAL);
        log.info("Remind upcoming jobs has started. future: {}", future);
        reminderJobProcessService.processAllUpcomingJobs(future);
        return new BaseResponse();
    }

}
