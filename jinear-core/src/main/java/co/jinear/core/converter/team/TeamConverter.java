package co.jinear.core.converter.team;

import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamConverter {

    Team map(TeamInitializeVo teamInitializeVo);
}
