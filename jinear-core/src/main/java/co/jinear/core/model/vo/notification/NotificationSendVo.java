package co.jinear.core.model.vo.notification;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationTemplateType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationSendVo {

    @EqualsAndHashCode.Include
    private String accountId;
    @EqualsAndHashCode.Include
    private NotificationTemplateType templateType;
    private Map<String, String> params;
    private LocaleType localeType;
}
