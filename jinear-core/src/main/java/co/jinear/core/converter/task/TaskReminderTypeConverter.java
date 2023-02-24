package co.jinear.core.converter.task;

import co.jinear.core.model.enumtype.task.TaskReminderType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TaskReminderTypeConverter implements AttributeConverter<TaskReminderType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskReminderType type) {
        return Optional.ofNullable(type).map(TaskReminderType::getValue).orElse(null);
    }

    @Override
    public TaskReminderType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TaskReminderType.values())
                .filter(repeatType -> repeatType.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
