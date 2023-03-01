package co.jinear.core.service.reminder;

import co.jinear.core.converter.reminder.ReminderConverter;
import co.jinear.core.converter.reminder.ReminderJobConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.entity.reminder.Reminder;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.model.vo.reminder.InitializeReminderJobVo;
import co.jinear.core.model.vo.reminder.ReminderInitializeVo;
import co.jinear.core.repository.reminder.ReminderRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.reminder.job.ReminderJobOperationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderOperationService {

    private final ReminderConverter reminderConverter;
    private final ReminderJobConverter reminderJobConverter;
    private final ReminderRepository reminderRepository;
    private final ReminderJobOperationService reminderJobOperationService;
    private final PassiveService passiveService;

    public ReminderDto initializeReminder(ReminderInitializeVo reminderInitializeVo) {
        log.info("Initialize reminder has started. reminderInitializeVo: {}", reminderInitializeVo);
        validateInitialRemindDateIsPresent(reminderInitializeVo);
        Reminder reminder = reminderConverter.map(reminderInitializeVo);
        Reminder saved = reminderRepository.save(reminder);
        log.info("Initialize reminder has completed. reminderId: {}", saved.getReminderId());
        initializeReminderJob(saved, reminderInitializeVo);
        return reminderConverter.map(saved);
    }

    @Transactional
    public String passivizeReminderAndAllActiveJobsWithExistingPassiveId(String reminderId, String passiveId) {
        log.info("Passivize reminder with existing passive id has started. reminderId: {},passiveId: {}", reminderId);
        updateAsPassive(reminderId, passiveId);
        passivizeAllActiveJobs(reminderId);
        log.info("Passivize reminder with existing passive id has completed. passiveId: {}", passiveId);
        return passiveId;
    }

    private void validateInitialRemindDateIsPresent(ReminderInitializeVo reminderInitializeVo) {
        if (Objects.isNull(reminderInitializeVo.getInitialRemindDate())) {
            throw new BusinessException();
        }
    }

    private void initializeReminderJob(Reminder reminder, ReminderInitializeVo reminderInitializeVo) {
        InitializeReminderJobVo initializeReminderJobVo = reminderJobConverter.map(reminder, reminderInitializeVo);
        reminderJobOperationService.initializeReminderJob(initializeReminderJobVo);
    }

    private Reminder retrieveEntity(String reminderId) {
        log.info("Retrieve entity has started. reminderId: {}", reminderId);
        return reminderRepository.findByReminderIdAndPassiveIdIsNull(reminderId)
                .orElseThrow(NotFoundException::new);
    }

    private String updateAsPassive(String reminderId, String passiveId) {
        Reminder reminder = retrieveEntity(reminderId);
        reminder.setPassiveId(passiveId);
        reminderRepository.save(reminder);
        return passiveId;
    }

    private void passivizeAllActiveJobs(String reminderId) {
        reminderJobOperationService.updateAllWithReminderId(reminderId, ReminderJobStatus.CANCELLED);
    }
}
