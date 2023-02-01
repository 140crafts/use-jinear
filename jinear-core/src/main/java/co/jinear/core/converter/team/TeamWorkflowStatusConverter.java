package co.jinear.core.converter.team;

import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.team.TeamWorkflowStatus;
import co.jinear.core.model.vo.team.workflow.InitializeTeamWorkflowStatusVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamWorkflowStatusConverter {

    TeamWorkflowStatusDto map(TeamWorkflowStatus teamWorkflowStatus);

    TeamWorkflowStatus map(InitializeTeamWorkflowStatusVo initializeTeamWorkflowStatusVo);
}
