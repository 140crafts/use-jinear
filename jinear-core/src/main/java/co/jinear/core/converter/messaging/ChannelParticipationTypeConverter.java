package co.jinear.core.converter.messaging;

import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ChannelParticipationTypeConverter implements AttributeConverter<ChannelParticipationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ChannelParticipationType type) {
        return Optional.ofNullable(type).map(ChannelParticipationType::getValue).orElse(null);
    }

    @Override
    public ChannelParticipationType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ChannelParticipationType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
