package co.jinear.core.model.dto.notification;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.notification.NotificationTemplateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class NotificationMessageDto extends BaseDto {

    private String notificationEventId;
    @Nullable
    private String title;
    @Nullable
    private String text;
    @Nullable
    private String launchUrl;
    private NotificationTemplateType templateType;
}
