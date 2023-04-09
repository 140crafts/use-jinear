package co.jinear.core.converter.notification;

import co.jinear.core.model.entity.notification.NotificationTarget;
import co.jinear.core.model.vo.notification.NotificationTargetInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeNotificationTargetVoConverter {

    NotificationTarget convert(NotificationTargetInitializeVo notificationTargetInitializeVo);
}
