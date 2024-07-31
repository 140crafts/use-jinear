package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Optional;

public class ProjectPriorityTypeConverter implements AttributeConverter<ProjectPriorityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectPriorityType type) {
        return Optional.ofNullable(type).map(ProjectPriorityType::getValue).orElse(null);
    }

    @Override
    public ProjectPriorityType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectPriorityType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}

