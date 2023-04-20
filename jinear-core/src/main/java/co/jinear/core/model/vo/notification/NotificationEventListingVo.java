package co.jinear.core.model.vo.notification;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationEventListingVo {
    private String workspaceId;
    private String currentAccountId;
    private int page;
}
