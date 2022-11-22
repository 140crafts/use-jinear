package co.jinear.core.validator.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.service.team.member.TeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static co.jinear.core.model.enumtype.team.TeamContentVisibilityType.HIDDEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamMemberService teamMemberService;

    public void validateHasAccess(String currentAccountId, TeamDto teamDto) {
        validateHasAccess(currentAccountId, teamDto.getTeamId());
    }

    public void validateHasAccess(String currentAccountId, String teamId) {
        teamMemberService.validateAccountTeamMember(currentAccountId, teamId);
    }

    public void validateHasContentAccess(String currentAccountId, TeamDto teamDto) {
        if (HIDDEN.equals(teamDto.getSettings().getContentVisibility())) {
            teamMemberService.validateAccountTeamMember(currentAccountId, teamDto.getTeamId());
        }
    }
}
