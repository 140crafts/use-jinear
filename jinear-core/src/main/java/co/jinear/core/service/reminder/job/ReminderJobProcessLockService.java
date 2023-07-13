package co.jinear.core.service.reminder.job;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderJobProcessLockService {
    private static final String KEY = "reminder-batch";
    private final LockService lockService;

    public void lockReminderJobProcessing() {
        log.info("Acquiring lock for processing reminder jobs has started.");
        lockService.lock(KEY, LockSourceType.REMINDER_JOB_PROCESS);
        log.info("Acquiring lock for processing reminder jobs has completed.");
    }

    public void unlockReminderJobProcessing() {
        log.info("Releasing lock for processing reminder jobs has started.");
        lockService.unlock(KEY, LockSourceType.REMINDER_JOB_PROCESS);
        log.info("Releasing lock for processing reminder jobs has completed.");
    }
}
