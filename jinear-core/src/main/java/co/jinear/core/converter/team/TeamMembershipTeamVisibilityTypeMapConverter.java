package co.jinear.core.converter.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public class TeamMembershipTeamVisibilityTypeMapConverter {

    @Named("membershipsToVisibilityTypeMap")
    public Map<TeamTaskVisibilityType, List<TeamMemberDto>> convert(List<TeamMemberDto> memberships) {
        Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap = new HashMap<>();
        memberships.forEach(teamMemberDto -> {
            TeamDto teamDto = teamMemberDto.getTeam();
            TeamTaskVisibilityType taskVisibility = teamDto.getTaskVisibility();
            List<TeamMemberDto> teamMembershipList = teamMemberMap.getOrDefault(taskVisibility, new ArrayList<>());
            teamMembershipList.add(teamMemberDto);
            teamMemberMap.put(taskVisibility, teamMembershipList);
        });
        return teamMemberMap;
    }
}
