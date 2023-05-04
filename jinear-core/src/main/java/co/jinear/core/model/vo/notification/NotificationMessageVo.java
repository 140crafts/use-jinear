package co.jinear.core.model.vo.notification;

import co.jinear.core.model.dto.notification.NotificationMessageExternalDataDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NotificationMessageVo {

    private String accountId;
    private String title;
    private String text;
    private String launchUrl;
    private LocaleType localeType;
    private List<String> targetIds;
    private Boolean isSilent;
    private NotificationMessageExternalDataDto data;
}
