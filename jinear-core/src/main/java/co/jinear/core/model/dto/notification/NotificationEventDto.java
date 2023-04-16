package co.jinear.core.model.dto.notification;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@ToString(callSuper = true)
public class NotificationEventDto extends BaseDto {

    private String notificationEventId;
    private Long notificationTemplateId;
    private String accountId;
    private Boolean isRead;
    private NotificationEventState eventState;
    private NotificationTemplateDto template;
    private Set<NotificationEventParamDto> params;
}
