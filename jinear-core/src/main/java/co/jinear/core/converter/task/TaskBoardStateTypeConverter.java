package co.jinear.core.converter.task;

import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TaskBoardStateTypeConverter implements AttributeConverter<TaskBoardStateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskBoardStateType type) {
        return Optional.ofNullable(type).map(TaskBoardStateType::getValue).orElse(null);
    }

    @Override
    public TaskBoardStateType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TaskBoardStateType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
