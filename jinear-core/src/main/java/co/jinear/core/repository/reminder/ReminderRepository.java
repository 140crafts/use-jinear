package co.jinear.core.repository.reminder;

import co.jinear.core.model.entity.reminder.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, String> {

    Optional<Reminder> findByReminderIdAndPassiveIdIsNull(String reminderId);
}
