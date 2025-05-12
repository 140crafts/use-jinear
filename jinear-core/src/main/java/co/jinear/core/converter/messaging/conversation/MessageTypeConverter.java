package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.model.enumtype.messaging.MessageType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MessageTypeConverter implements AttributeConverter<MessageType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MessageType type) {
        return Optional.ofNullable(type).map(MessageType::getValue).orElse(null);
    }

    @Override
    public MessageType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MessageType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}

