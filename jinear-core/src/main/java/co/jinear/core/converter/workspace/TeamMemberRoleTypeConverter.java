package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TeamMemberRoleTypeConverter implements AttributeConverter<TeamMemberRoleType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TeamMemberRoleType type) {
        return Optional.ofNullable(type).map(TeamMemberRoleType::getValue).orElse(null);
    }

    @Override
    public TeamMemberRoleType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TeamMemberRoleType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
