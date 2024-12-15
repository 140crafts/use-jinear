package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectDomainCnameCheckResultType;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Optional;

public class ProjectDomainCnameCheckTypeConverter implements AttributeConverter<ProjectDomainCnameCheckResultType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectDomainCnameCheckResultType type) {
        return Optional.ofNullable(type).map(ProjectDomainCnameCheckResultType::getValue).orElse(null);
    }

    @Override
    public ProjectDomainCnameCheckResultType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectDomainCnameCheckResultType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}

