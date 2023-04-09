package co.jinear.core.converter.notification;

import co.jinear.core.model.enumtype.notification.NotificationTargetType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class NotificationTargetTypeConverter implements AttributeConverter<NotificationTargetType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotificationTargetType type) {
        return Optional.ofNullable(type).map(NotificationTargetType::getValue).orElse(null);
    }

    @Override
    public NotificationTargetType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(NotificationTargetType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
