package co.jinear.core.converter.messaging;

import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ChannelVisibilityTypeConverter implements AttributeConverter<ChannelVisibilityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ChannelVisibilityType type) {
        return Optional.ofNullable(type).map(ChannelVisibilityType::getValue).orElse(null);
    }

    @Override
    public ChannelVisibilityType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ChannelVisibilityType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
