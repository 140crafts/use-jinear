package co.jinear.core.repository.reminder;

import co.jinear.core.model.entity.reminder.ReminderJob;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ReminderJobRepository extends JpaRepository<ReminderJob, String> {

    Optional<ReminderJob> findByReminderJobIdAndPassiveIdIsNull(String reminderJobId);

    Optional<ReminderJob> findFirstByReminderIdAndReminderJobStatusAndPassiveIdIsNullOrderByDateAsc(String reminderJobId, ReminderJobStatus reminderJobStatus);

    List<ReminderJob> findAllByReminderJobStatusAndDateLessThanAndPassiveIdIsNull(ReminderJobStatus reminderJobStatus, ZonedDateTime date);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ReminderJob reminderJob
                set reminderJob.reminderJobStatus=:reminderJobStatus
                    where
                        reminderJob.reminderId=:reminderId and
                        reminderJob.passiveId is null
                """)
    void updateAllWithReminderId(@Param("reminderId") String reminderId, @Param("reminderJobStatus") ReminderJobStatus reminderJobStatus);

    Long countAllByReminderIdAndDateAndPassiveIdIsNull(String reminderId,ZonedDateTime dateTime);
}
