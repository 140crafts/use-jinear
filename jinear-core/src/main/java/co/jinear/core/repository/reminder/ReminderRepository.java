package co.jinear.core.repository.reminder;

import co.jinear.core.model.entity.reminder.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, String> {
}
