package co.jinear.core.converter.reminder;

import co.jinear.core.model.enumtype.reminder.ReminderType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ReminderTypeConverter implements AttributeConverter<ReminderType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReminderType reminderType) {
        return Optional.ofNullable(reminderType).map(ReminderType::getValue).orElse(null);
    }

    @Override
    public ReminderType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ReminderType.values())
                .filter(reminderType -> reminderType.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
