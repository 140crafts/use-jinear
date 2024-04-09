package co.jinear.core.converter.messaging;

import co.jinear.core.model.enumtype.messaging.MessageReactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MessageReactionTypeConverter implements AttributeConverter<MessageReactionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MessageReactionType type) {
        return Optional.ofNullable(type).map(MessageReactionType::getValue).orElse(null);
    }

    @Override
    public MessageReactionType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MessageReactionType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
