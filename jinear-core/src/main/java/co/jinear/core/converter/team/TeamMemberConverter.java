package co.jinear.core.converter.team;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.entity.team.TeamMember;
import co.jinear.core.model.request.team.AddTeamMemberRequest;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMemberConverter {

    @Mapping(source = "account.username.username",target = "account.username")
    TeamMemberDto map(TeamMember teamMember);

    TeamMember map(TeamMemberAddVo teamMemberAddVo);

    TeamMemberAddVo map(AddTeamMemberRequest addTeamMemberRequest);
}
