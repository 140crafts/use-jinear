package co.jinear.core.converter.notification;

import co.jinear.core.model.enumtype.notification.NotificationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class NotificationTypeConverter implements AttributeConverter<NotificationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotificationType type) {
        return Optional.ofNullable(type).map(NotificationType::getValue).orElse(null);
    }

    @Override
    public NotificationType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(NotificationType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
