package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.TaskReminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskReminderRepository extends JpaRepository<TaskReminder,String> {

}
