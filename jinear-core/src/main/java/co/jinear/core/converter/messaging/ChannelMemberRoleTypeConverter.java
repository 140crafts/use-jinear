package co.jinear.core.converter.messaging;

import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ChannelMemberRoleTypeConverter implements AttributeConverter<ChannelMemberRoleType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ChannelMemberRoleType type) {
        return Optional.ofNullable(type).map(ChannelMemberRoleType::getValue).orElse(null);
    }

    @Override
    public ChannelMemberRoleType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ChannelMemberRoleType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
