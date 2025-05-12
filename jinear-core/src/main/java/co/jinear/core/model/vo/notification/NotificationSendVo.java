package co.jinear.core.model.vo.notification;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationSendVo {

    @EqualsAndHashCode.Include
    private String accountId;
    private String workspaceId;
    private String teamId;
    private String taskId;
    private String threadId;
    private String taskTag;
    private String title;
    private String text;
    private String launchUrl;
    private LocaleType localeType;
    private Boolean isSilent;
    private NotificationType notificationType;
    private String relatedTaskTag;
    private String senderSessionId;
}
