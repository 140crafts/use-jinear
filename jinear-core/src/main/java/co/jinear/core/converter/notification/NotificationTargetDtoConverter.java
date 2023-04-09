package co.jinear.core.converter.notification;


import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.entity.notification.NotificationTarget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationTargetDtoConverter {

    NotificationTargetDto convert(NotificationTarget notificationTarget);
}
