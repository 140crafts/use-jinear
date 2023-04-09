package co.jinear.core.model.dto.notification;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.notification.NotificationTargetType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationTargetDto extends BaseDto {

    private String externalTargetId;
    private String accountId;
    private String sessionInfoId;
    private NotificationTargetType targetType;
}
