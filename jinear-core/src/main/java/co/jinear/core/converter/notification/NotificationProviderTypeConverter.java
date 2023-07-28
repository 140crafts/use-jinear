package co.jinear.core.converter.notification;

import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class NotificationProviderTypeConverter implements AttributeConverter<NotificationProviderType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotificationProviderType type) {
        return Optional.ofNullable(type).map(NotificationProviderType::getValue).orElse(null);
    }

    @Override
    public NotificationProviderType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(NotificationProviderType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
