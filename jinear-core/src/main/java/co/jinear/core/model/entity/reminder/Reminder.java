package co.jinear.core.model.entity.reminder;

import co.jinear.core.converter.reminder.ReminderTypeConverter;
import co.jinear.core.converter.reminder.RepeatTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.reminder.ReminderType;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "reminder")
public class Reminder extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "reminder_id")
    private String reminderId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Convert(converter = ReminderTypeConverter.class)
    @Column(name = "type")
    private ReminderType type;

    @Convert(converter = RepeatTypeConverter.class)
    @Column(name = "repeat_type")
    private RepeatType repeatType;

    @Column(name = "repeat_start")
    private ZonedDateTime repeatStart;

    @Column(name = "repeat_end")
    private ZonedDateTime repeatEnd;
}
