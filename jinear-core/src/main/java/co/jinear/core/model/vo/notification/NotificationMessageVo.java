package co.jinear.core.model.vo.notification;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NotificationMessageVo {

    private String title;
    private String text;
    private String launchUrl;
    private LocaleType localeType;
    private List<String> targetIds;
}
