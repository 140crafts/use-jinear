package co.jinear.core.converter.reminder;

import co.jinear.core.model.enumtype.reminder.ReminderJobStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ReminderJobStatusConverter implements AttributeConverter<ReminderJobStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReminderJobStatus reminderJobStatus) {
        return Optional.ofNullable(reminderJobStatus).map(ReminderJobStatus::getValue).orElse(null);
    }

    @Override
    public ReminderJobStatus convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ReminderJobStatus.values())
                .filter(reminderJobStatus -> reminderJobStatus.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
