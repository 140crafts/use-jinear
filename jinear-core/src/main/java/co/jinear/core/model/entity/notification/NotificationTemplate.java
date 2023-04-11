package co.jinear.core.model.entity.notification;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationTemplateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "notification_template")
public class NotificationTemplate extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "notification_template_id")
    private Long notificationTemplateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "template_type")
    private NotificationTemplateType templateType;

    @Enumerated(EnumType.STRING)
    @Column(name = "locale")
    private LocaleType localeType;

    @Column(name = "template_title")
    private String title;

    @Column(name = "template_text")
    private String text;

    @Column(name = "launch_url")
    private String launchUrl;
}
