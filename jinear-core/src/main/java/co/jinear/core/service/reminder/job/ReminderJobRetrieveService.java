package co.jinear.core.service.reminder.job;

import co.jinear.core.converter.reminder.ReminderJobConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.entity.reminder.ReminderJob;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.repository.reminder.ReminderJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderJobRetrieveService {

    private final ReminderJobRepository reminderJobRepository;
    private final ReminderJobConverter reminderJobConverter;

    public ReminderJobDto retrieve(String reminderJobId) {
        log.info("Reminder job retrieve has started. reminderJobId: {}", reminderJobId);
        return reminderJobRepository.findByReminderJobIdAndPassiveIdIsNull(reminderJobId)
                .map(reminderJobConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public ReminderJob retrieveEntity(String reminderJobId) {
        log.info("Reminder job retrieve has started. reminderJobId: {}", reminderJobId);
        return reminderJobRepository.findByReminderJobIdAndPassiveIdIsNull(reminderJobId)
                .orElseThrow(NotFoundException::new);
    }

    public ReminderJobDto findFirstReminderJob(String reminderId, ReminderJobStatus reminderJobStatus) {
        log.info("Find first reminder job by reminder id and status has started. reminderId: {}, reminderJobStatus: {}", reminderId, reminderJobStatus);
        return reminderJobRepository.findFirstByReminderIdAndReminderJobStatusAndPassiveIdIsNullOrderByDateAsc(reminderId, reminderJobStatus)
                .map(reminderJobConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Map<String, ZonedDateTime> retrieveEarliestDates(List<String> reminderIds, ReminderJobStatus reminderJobStatus) {
        log.info("Retrieve earliest reminder job dates has started. reminderIds: {}, reminderJobStatus: {}", reminderIds, reminderJobStatus);
        if (reminderIds.isEmpty()) {
            return Map.of();
        }
        List<ReminderJob> reminderJobs = reminderJobRepository.findAllByReminderIdInAndReminderJobStatusAndPassiveIdIsNull(reminderIds, reminderJobStatus);
        return reminderJobs.stream()
                .collect(Collectors.toMap(ReminderJob::getReminderId, ReminderJob::getDate, BinaryOperator.minBy(Comparator.naturalOrder())));
    }

    public boolean checkIfAnyReminderJobExistsForReminderAtDate(String reminderId, ZonedDateTime nextDate) {
        return reminderJobRepository.countAllByReminderIdAndDateAndPassiveIdIsNull(reminderId, nextDate) > 0L;
    }
}
