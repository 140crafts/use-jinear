package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ProjectFeedAccessTypeConverter implements AttributeConverter<ProjectFeedAccessType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectFeedAccessType type) {
        return Optional.ofNullable(type).map(ProjectFeedAccessType::getValue).orElse(null);
    }

    @Override
    public ProjectFeedAccessType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectFeedAccessType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
