package co.jinear.core.converter.notification;

import co.jinear.core.model.enumtype.notification.NotificationEventState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class NotificationEventStateTypeConverter implements AttributeConverter<NotificationEventState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotificationEventState type) {
        return Optional.ofNullable(type).map(NotificationEventState::getValue).orElse(null);
    }

    @Override
    public NotificationEventState convertToEntityAttribute(Integer integer) {
        return Arrays.stream(NotificationEventState.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
