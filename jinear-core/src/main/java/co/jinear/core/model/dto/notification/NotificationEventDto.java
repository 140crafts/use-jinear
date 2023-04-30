package co.jinear.core.model.dto.notification;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import co.jinear.core.model.enumtype.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class NotificationEventDto extends BaseDto {

    private String notificationEventId;
    private String accountId;
    private Boolean isRead;
    private NotificationEventState eventState;
    private NotificationType notificationType;
    private LocaleType localeType;
    private String title;
    private String text;
    private String launchUrl;
}
