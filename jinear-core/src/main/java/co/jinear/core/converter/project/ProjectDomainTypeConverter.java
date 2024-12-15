package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectDomainType;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Optional;

public class ProjectDomainTypeConverter implements AttributeConverter<ProjectDomainType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectDomainType type) {
        return Optional.ofNullable(type).map(ProjectDomainType::getValue).orElse(null);
    }

    @Override
    public ProjectDomainType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectDomainType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}

