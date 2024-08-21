package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectStateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ProjectStateTypeConverter implements AttributeConverter<ProjectStateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectStateType type) {
        return Optional.ofNullable(type).map(ProjectStateType::getValue).orElse(null);
    }

    @Override
    public ProjectStateType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectStateType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
