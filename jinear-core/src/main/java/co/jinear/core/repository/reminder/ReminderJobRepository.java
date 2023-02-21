package co.jinear.core.repository.reminder;

import co.jinear.core.model.entity.reminder.ReminderJob;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ReminderJobRepository extends JpaRepository<ReminderJob, String> {

    Optional<ReminderJob> findByReminderJobIdAndPassiveIdIsNull(String reminderJobId);

    List<ReminderJob> findAllByReminderJobStatusAndDateLessThanAndPassiveIdIsNull(ReminderJobStatus reminderJobStatus, ZonedDateTime date);
}
