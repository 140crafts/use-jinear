package co.jinear.core.converter.task;

import co.jinear.core.converter.team.TeamMembershipTeamVisibilityTypeMapConverter;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskFilterRequestConverter {

    @Autowired
    protected TeamMembershipTeamVisibilityTypeMapConverter teamMembershipTeamVisibilityTypeMapConverter;

    @Mapping(target = "teamMemberMap", expression = "java(teamMembershipTeamVisibilityTypeMapConverter.convert(memberships))")
    public abstract TaskSearchFilterVo convert(TaskFilterRequest taskFilterRequest, List<TeamMemberDto> memberships);

    @Mapping(target = "teamMemberMap", ignore = true)
    public abstract TaskSearchFilterVo convert(TaskFilterRequest taskFilterRequest);
}
