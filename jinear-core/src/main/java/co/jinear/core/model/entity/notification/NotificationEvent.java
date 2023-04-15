package co.jinear.core.model.entity.notification;

import co.jinear.core.converter.notification.NotificationEventStateTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "notification_event")
public class NotificationEvent extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "notification_event_id")
    private String notificationEventId;

    @Column(name = "notification_template_id")
    private Long notificationTemplateId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "is_read")
    private Boolean isRead;

    @Convert(converter = NotificationEventStateTypeConverter.class)
    @Column(name = "event_state")
    private NotificationEventState eventState;

    @ManyToOne
    @JoinColumn(name = "notification_template_id", insertable = false, updatable = false)
    private NotificationTemplate template;

    @OneToMany(mappedBy = "notificationEvent")
    @Where(clause = "passive_id is null")
    private Set<NotificationEventParam> params;
}
