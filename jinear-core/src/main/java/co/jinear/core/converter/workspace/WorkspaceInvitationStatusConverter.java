package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class WorkspaceInvitationStatusConverter implements AttributeConverter<WorkspaceInvitationStatusType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WorkspaceInvitationStatusType type) {
        return Optional.ofNullable(type).map(WorkspaceInvitationStatusType::getValue).orElse(null);
    }

    @Override
    public WorkspaceInvitationStatusType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(WorkspaceInvitationStatusType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
