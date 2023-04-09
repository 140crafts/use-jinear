package co.jinear.core.model.vo.notification;

import co.jinear.core.model.enumtype.notification.NotificationTargetType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NotificationTargetInitializeVo {

    private String externalTargetId;
    private String accountId;
    private String sessionInfoId;
    private NotificationTargetType targetType;
}
