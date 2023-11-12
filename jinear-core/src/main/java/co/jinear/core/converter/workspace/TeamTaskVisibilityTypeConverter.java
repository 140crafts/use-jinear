package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class TeamTaskVisibilityTypeConverter implements AttributeConverter<TeamTaskVisibilityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TeamTaskVisibilityType type) {
        return Optional.ofNullable(type).map(TeamTaskVisibilityType::getValue).orElse(null);
    }

    @Override
    public TeamTaskVisibilityType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(TeamTaskVisibilityType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
