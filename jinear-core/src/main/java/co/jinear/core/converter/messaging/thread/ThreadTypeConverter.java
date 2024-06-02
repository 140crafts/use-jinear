package co.jinear.core.converter.messaging.thread;

import co.jinear.core.model.enumtype.messaging.ThreadType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ThreadTypeConverter implements AttributeConverter<ThreadType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ThreadType type) {
        return Optional.ofNullable(type).map(ThreadType::getValue).orElse(null);
    }

    @Override
    public ThreadType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ThreadType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
