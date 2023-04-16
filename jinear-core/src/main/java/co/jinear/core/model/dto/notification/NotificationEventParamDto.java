package co.jinear.core.model.dto.notification;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class NotificationEventParamDto extends BaseDto {

    private String notificationEventParamId;
    private String notificationEventId;
    private String paramKey;
    private String paramValue;
}
