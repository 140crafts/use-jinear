package co.jinear.core.service.reminder.job;

import co.jinear.core.converter.reminder.ReminderJobConverter;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import co.jinear.core.repository.reminder.ReminderJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderJobListingService {

    private final ReminderJobRepository reminderJobRepository;
    private final ReminderJobConverter reminderJobConverter;

    public List<ReminderJobDto> retrieveAllByReminderJobStatusAndBeforeDate(ReminderJobStatus reminderJobStatus, ZonedDateTime date) {
        log.info("Retrieve all reminder jobs by reminder job status and before date has started. reminderJobStatus: {}, date: {}", reminderJobStatus, date);
        List<ReminderJobDto> jobDtos = reminderJobRepository.findAllByReminderJobStatusAndDateLessThanAndPassiveIdIsNull(reminderJobStatus, date)
                .stream()
                .map(reminderJobConverter::map)
                .toList();
        log.info("Found {} pending jobs.", jobDtos.size());
        return jobDtos;
    }
}
