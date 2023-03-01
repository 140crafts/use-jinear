package co.jinear.core.model.entity.task;

import co.jinear.core.converter.task.TaskReminderTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.reminder.Reminder;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "task_reminder")
public class TaskReminder extends BaseEntity {

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

    @OneToOne
    @JoinColumn(name = "reminder_id", insertable = false, updatable = false)
    private Reminder reminder;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;
}
