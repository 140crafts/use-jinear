package co.jinear.core.model.dto.notification;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class NotificationTemplateDto extends BaseDto {

    private Long notificationTemplateId;
    private String templateName;
    private String title;
    private String text;
    private String launchUrl;
    private LocaleType localeType;
}
