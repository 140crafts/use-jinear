package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.team.TeamTaskSourceType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TeamTaskSourceTypeConverter implements AttributeConverter<TeamTaskSourceType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TeamTaskSourceType type) {
        return Optional.ofNullable(type).map(TeamTaskSourceType::getValue).orElse(null);
    }

    @Override
    public TeamTaskSourceType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TeamTaskSourceType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
