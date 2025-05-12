package co.jinear.core.model.dto.notification;

import co.jinear.core.model.enumtype.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationMessageExternalDataDto {

    private String workspaceId;
    private String teamId;
    private String taskId;
    private String taskTag;
    private NotificationType notificationType;
    private String senderSessionId;
}
