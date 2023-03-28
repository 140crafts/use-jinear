package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class WorkspaceAccountRoleTypeConverter implements AttributeConverter<WorkspaceAccountRoleType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WorkspaceAccountRoleType type) {
        return Optional.ofNullable(type).map(WorkspaceAccountRoleType::getValue).orElse(null);
    }

    @Override
    public WorkspaceAccountRoleType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(WorkspaceAccountRoleType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}

