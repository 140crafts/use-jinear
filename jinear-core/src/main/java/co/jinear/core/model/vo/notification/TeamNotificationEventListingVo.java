package co.jinear.core.model.vo.notification;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TeamNotificationEventListingVo extends NotificationEventListingVo {
    private String teamId;
}
