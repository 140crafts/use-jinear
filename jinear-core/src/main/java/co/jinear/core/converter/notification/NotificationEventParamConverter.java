package co.jinear.core.converter.notification;

import co.jinear.core.model.entity.notification.NotificationEventParam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEventParamConverter {

    NotificationEventParam map(String notificationEventId, String paramKey, String paramValue);
}
