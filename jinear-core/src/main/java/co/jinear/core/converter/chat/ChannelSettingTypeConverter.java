package co.jinear.core.converter.chat;

import co.jinear.core.model.enumtype.chat.ChannelSettingType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ChannelSettingTypeConverter implements AttributeConverter<ChannelSettingType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ChannelSettingType type) {
        return Optional.ofNullable(type).map(ChannelSettingType::getValue).orElse(null);
    }

    @Override
    public ChannelSettingType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ChannelSettingType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
