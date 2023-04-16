package co.jinear.core.converter.notification;


import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.entity.notification.NotificationEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEventDtoConverter {

    NotificationEventDto convert(NotificationEvent notificationEvent);
}
