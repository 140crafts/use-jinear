package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.ProjectPostCommentPolicyType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class ProjectPostCommentPolicyTypeConverter implements AttributeConverter<ProjectPostCommentPolicyType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectPostCommentPolicyType type) {
        return Optional.ofNullable(type).map(ProjectPostCommentPolicyType::getValue).orElse(null);
    }

    @Override
    public ProjectPostCommentPolicyType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(ProjectPostCommentPolicyType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
