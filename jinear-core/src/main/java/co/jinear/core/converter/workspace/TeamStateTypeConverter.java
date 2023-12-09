package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.team.TeamStateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TeamStateTypeConverter implements AttributeConverter<TeamStateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TeamStateType type) {
        return Optional.ofNullable(type).map(TeamStateType::getValue).orElse(null);
    }

    @Override
    public TeamStateType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TeamStateType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
