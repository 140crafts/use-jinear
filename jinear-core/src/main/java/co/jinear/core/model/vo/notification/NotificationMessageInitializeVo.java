package co.jinear.core.model.vo.notification;

import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotificationMessageInitializeVo {
    private LocaleType localeType;
    private NotificationTemplateDto notificationTemplateDto;
    private Map<String, String> params;
}
