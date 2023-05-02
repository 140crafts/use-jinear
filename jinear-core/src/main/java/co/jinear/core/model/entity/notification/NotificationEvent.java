package co.jinear.core.model.entity.notification;

import co.jinear.core.converter.notification.NotificationEventStateTypeConverter;
import co.jinear.core.converter.notification.NotificationTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import co.jinear.core.model.enumtype.notification.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_silent")
    private Boolean isSilent;

    @Convert(converter = NotificationEventStateTypeConverter.class)
    @Column(name = "event_state")
    private NotificationEventState eventState;

    @Convert(converter = NotificationTypeConverter.class)
    @Column(name = "type")
    private NotificationType notificationType;

    @Column(name = "locale")
    private LocaleType localeType;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "launch_url")
    private String launchUrl;
}
