package co.jinear.core.converter.notification;

import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.entity.notification.NotificationTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationTemplateDtoConverter {

    NotificationTemplateDto convert(NotificationTemplate notificationTemplate);
}
