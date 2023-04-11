package co.jinear.core.model.entity.notification;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "notification_event_param")
public class NotificationEventParam {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "notification_event_param_id")
    private String notificationEventParamId;

    @Column(name = "notification_event_id")
    private String notificationEventId;

    @Column(name = "param_key")
    private String paramKey;

    @Column(name = "param_value")
    private String paramValue;

    @ManyToOne
    @JoinColumn(name = "notification_event_id", insertable = false, updatable = false)
    private NotificationEvent notificationEvent;
}
