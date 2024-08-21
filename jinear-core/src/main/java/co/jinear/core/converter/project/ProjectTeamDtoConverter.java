package co.jinear.core.converter.project;

import co.jinear.core.converter.team.TeamDtoConverter;
import co.jinear.core.model.dto.project.ProjectTeamDto;
import co.jinear.core.model.entity.project.ProjectTeam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TeamDtoConverter.class})
public interface ProjectTeamDtoConverter {

    ProjectTeamDto convert(ProjectTeam projectTeam);
}
