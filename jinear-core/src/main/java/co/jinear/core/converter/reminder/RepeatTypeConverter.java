package co.jinear.core.converter.reminder;

import co.jinear.core.model.enumtype.reminder.RepeatType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class RepeatTypeConverter implements AttributeConverter<RepeatType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RepeatType repeatType) {
        return Optional.ofNullable(repeatType).map(RepeatType::getValue).orElse(null);
    }

    @Override
    public RepeatType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(RepeatType.values())
                .filter(repeatType -> repeatType.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
