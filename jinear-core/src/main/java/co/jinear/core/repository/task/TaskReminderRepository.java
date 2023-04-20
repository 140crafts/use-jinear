package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.TaskReminder;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskReminderRepository extends JpaRepository<TaskReminder, String> {

    Optional<TaskReminder> findByTaskReminderIdAndPassiveIdIsNull(String taskReminderId);

    Optional<TaskReminder> findFirstByTaskIdAndTaskReminderTypeAndPassiveIdIsNull(String taskId, TaskReminderType taskReminderType);

    List<TaskReminder> findAllByTaskIdAndPassiveIdIsNull(String taskId);

    Optional<TaskReminder> findByTaskIdAndReminderIdAndPassiveIdIsNull(String taskId, String reminderId);
}
