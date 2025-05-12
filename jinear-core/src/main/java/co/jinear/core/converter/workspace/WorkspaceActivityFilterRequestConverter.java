package co.jinear.core.converter.workspace;

import co.jinear.core.converter.team.TeamMembershipTeamVisibilityTypeMapConverter;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.request.workspace.WorkspaceActivityFilterRequest;
import co.jinear.core.model.vo.workspace.WorkspaceActivityFilterVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class WorkspaceActivityFilterRequestConverter {

    @Autowired
    protected TeamMembershipTeamVisibilityTypeMapConverter teamMembershipTeamVisibilityTypeMapConverter;

    @Mapping(target = "teamMemberMap", expression = "java(teamMembershipTeamVisibilityTypeMapConverter.convert(memberships))")
    public abstract WorkspaceActivityFilterVo convert(WorkspaceActivityFilterRequest workspaceActivityFilterRequest, List<TeamMemberDto> memberships);

}
