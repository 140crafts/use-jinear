package co.jinear.core.manager.team;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.member.TeamMemberListingService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberManager {

    private final TeamMemberListingService teamMemberListingService;
    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;

    public TeamMemberListingResponse retrieveTeamMembers(String teamId,int page) {
        String accountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(accountId, teamId);
        log.info("Retrieve team members has started. teamId: {}, page: {}, accountId: {}", teamId,page, accountId);
        Page<TeamMemberDto> teamMemberDtoPage = teamMemberListingService.retrieveTeamMembers(teamId,page);
        return mapToResponse(teamMemberDtoPage);
    }

    private TeamMemberListingResponse mapToResponse(Page<TeamMemberDto> teamMemberDtoPage) {
        TeamMemberListingResponse teamMemberListingResponse = new TeamMemberListingResponse();
        teamMemberListingResponse.setTeamMemberDtoList(new PageDto<>(teamMemberDtoPage));
        return teamMemberListingResponse;
    }
}
