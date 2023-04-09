package co.jinear.core.converter.notification;

import co.jinear.core.model.request.notification.NotificationTargetInitializeRequest;
import co.jinear.core.model.vo.notification.NotificationTargetInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationTargetInitializeRequestConverter {

    NotificationTargetInitializeVo convert(NotificationTargetInitializeRequest notificationTargetInitializeRequest, String accountId, String sessionInfoId);
}
