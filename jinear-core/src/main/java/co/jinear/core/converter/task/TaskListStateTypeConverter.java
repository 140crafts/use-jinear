package co.jinear.core.converter.task;

import co.jinear.core.model.enumtype.task.TaskListStateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TaskListStateTypeConverter implements AttributeConverter<TaskListStateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskListStateType type) {
        return Optional.ofNullable(type).map(TaskListStateType::getValue).orElse(null);
    }

    @Override
    public TaskListStateType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TaskListStateType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
