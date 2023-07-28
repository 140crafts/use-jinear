package co.jinear.core.model.vo.notification;

import co.jinear.core.model.dto.notification.NotificationMessageExternalDataDto;
import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationMessageVo {

    private String accountId;
    private String title;
    private String text;
    private String launchUrl;
    private LocaleType localeType;
    private NotificationTargetDto target;
    private Boolean isSilent;
    private NotificationMessageExternalDataDto data;
}
