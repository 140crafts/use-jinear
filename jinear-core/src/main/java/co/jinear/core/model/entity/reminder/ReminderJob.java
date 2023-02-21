package co.jinear.core.model.entity.reminder;

import co.jinear.core.converter.reminder.ReminderJobStatusConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "reminder_job")
public class ReminderJob extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "reminder_job_id")
    private String reminderJobId;

    @Column(name = "reminder_id")
    private String reminderId;

    @Column(name = "date")
    private ZonedDateTime date;

    @Convert(converter = ReminderJobStatusConverter.class)
    @Column(name = "status")
    private ReminderJobStatus reminderJobStatus;

    @ManyToOne
    @JoinColumn(name = "reminder_id", insertable = false, updatable = false)
    private Reminder reminder;
}
