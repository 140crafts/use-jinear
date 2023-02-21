package co.jinear.core.service.reminder.job;

import co.jinear.core.converter.reminder.ReminderJobConverter;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.entity.reminder.ReminderJob;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.model.vo.reminder.InitializeReminderJobVo;
import co.jinear.core.repository.reminder.ReminderJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderJobOperationService {

    private final ReminderJobRepository reminderJobRepository;
    private final ReminderJobConverter reminderJobConverter;
    private final ReminderJobRetrieveService reminderJobRetrieveService;

    public ReminderJobDto initializeReminderJob(InitializeReminderJobVo initializeReminderJobVo) {
        log.info("Initialize reminder job has started. initializeReminderJobVo: {}", initializeReminderJobVo);
        ReminderJob reminderJob = reminderJobConverter.map(initializeReminderJobVo);
        ReminderJob saved = reminderJobRepository.save(reminderJob);
        log.info("Initialize reminder job has completed. reminderJobId: {}", saved.getReminderJobId());
        return reminderJobConverter.map(saved);
    }

    public void updateReminderJobStatus(String reminderJobId, ReminderJobStatus reminderJobStatus) {
        log.info("Update reminder job status has started. reminderJobId: {}", reminderJobId);
        ReminderJob reminderJob = reminderJobRetrieveService.retrieveEntity(reminderJobId);
        log.info("Old reminder job status: {}, new reminder job status: {}", reminderJob.getReminderJobStatus(), reminderJobStatus);
        reminderJob.setReminderJobStatus(reminderJobStatus);
        ReminderJob saved = reminderJobRepository.save(reminderJob);
        log.info("Update reminder job status has completed. reminderJobId: {}", saved.getReminderJobId());
    }
}
