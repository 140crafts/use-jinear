package co.jinear.core.model.vo.notification;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class NotificationTemplatePopulateVo {

    private NotificationType templateType;
    private Map<String, String> params;
    private LocaleType localeType;
}
