package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ProjectPostInitializeAccessTypeConverter implements AttributeConverter<ProjectPostInitializeAccessType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectPostInitializeAccessType type) {
        return Optional.ofNullable(type).map(ProjectPostInitializeAccessType::getValue).orElse(null);
    }

    @Override
    public ProjectPostInitializeAccessType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectPostInitializeAccessType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
