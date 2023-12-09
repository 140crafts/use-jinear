package co.jinear.core.converter.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.entity.team.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamDtoConverter {

    TeamDto map(Team team);
}
