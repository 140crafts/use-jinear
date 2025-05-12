package co.jinear.core.model.entity.notification;

import co.jinear.core.converter.notification.NotificationProviderTypeConverter;
import co.jinear.core.converter.notification.NotificationTargetTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import co.jinear.core.model.enumtype.notification.NotificationTargetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "notification_target")
public class NotificationTarget extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "notification_target_id")
    private String notificationTargetId;

    @Column(name = "external_target_id")
    private String externalTargetId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "session_info_id")
    private String sessionInfoId;

    @Convert(converter = NotificationTargetTypeConverter.class)
    @Column(name = "target_type")
    private NotificationTargetType targetType;

    @Convert(converter = NotificationProviderTypeConverter.class)
    @Column(name = "provider")
    private NotificationProviderType providerType;
}
