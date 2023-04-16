package co.jinear.core.service.notification.provider;

import co.jinear.core.model.vo.notification.NotificationMessageVo;

public interface NotificationClient {

    void send(NotificationMessageVo notificationMessageVo) throws Exception;
}
