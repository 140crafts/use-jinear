package co.jinear.core.model.entity.task;

import co.jinear.core.converter.task.TaskReminderTypeConverter;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "task_reminder")
public class TaskReminder {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_reminder_id")
    private String taskReminderId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "reminder_id")
    private String reminderId;

    @Convert(converter = TaskReminderTypeConverter.class)
    @Column(name = "task_reminder_type")
    private TaskReminderType taskReminderType;
}
