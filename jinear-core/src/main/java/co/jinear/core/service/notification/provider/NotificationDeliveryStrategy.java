package co.jinear.core.service.notification.provider;

import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import co.jinear.core.model.vo.notification.NotificationMessageVo;

public interface NotificationDeliveryStrategy {

    void send(NotificationMessageVo notificationMessageVo) throws Exception;

    NotificationProviderType getProviderType();
}
