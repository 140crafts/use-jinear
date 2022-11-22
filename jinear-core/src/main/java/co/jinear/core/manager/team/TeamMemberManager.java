package co.jinear.core.manager.team;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamMemberListingBaseResponse;
import co.jinear.core.model.vo.team.DeleteTeamMemberVo;
import co.jinear.core.model.vo.team.InitializeTeamMemberVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.member.TeamMemberListingService;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.validator.team.TeamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberManager {

    private final int TEAM_MEMBER_PAGE_SIZE = 50;

    private final TeamRetrieveService teamRetrieveService;
    private final TeamMemberService teamMemberService;
    private final SessionInfoService sessionInfoService;
    private final TeamMemberListingService teamMemberListingService;
    private final TeamValidator teamValidator;

    public TeamMemberListingBaseResponse retrieveTeamMembers(String teamUsername, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve team members has started. teamUsername: {}, page: {}, currentAccountId: {}", teamUsername, page, currentAccountId);
        TeamDto teamDto = teamRetrieveService.retrieveTeamWithUsername(teamUsername);
        teamValidator.validateHasAccess(currentAccountId, teamDto);
        Page<TeamMemberDto> teamMemberDtoPage = decideAndRetrievePage(page, currentAccountId, teamDto);
        return mapValues(teamMemberDtoPage);
    }

    public BaseResponse joinTeam(String teamUsername) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Join team has started. teamUsername: {}, currentAccountId: {}", teamUsername, currentAccountId);
        TeamDto teamDto = teamRetrieveService.retrieveTeamWithUsername(teamUsername);
        InitializeTeamMemberVo initializeTeamMemberVo = mapValues(currentAccountId, teamDto);
        teamMemberService.initializeTeamMember(initializeTeamMemberVo);
        return new BaseResponse();
    }

    public BaseResponse leaveTeam(String teamUsername) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Leave team has started. teamUsername: {}, currentAccountId: {}", teamUsername, currentAccountId);
        TeamDto teamDto = teamRetrieveService.retrieveTeamWithUsername(teamUsername);
        teamMemberService.validateAccountIsNotTeamOwner(currentAccountId, teamDto.getTeamId());
        DeleteTeamMemberVo deleteTeamMemberVo = mapToDeleteTeamMember(currentAccountId, teamDto);
        teamMemberService.deleteTeamMember(deleteTeamMemberVo);
        return new BaseResponse();
    }

    private Page<TeamMemberDto> decideAndRetrievePage(Integer page, String currentAccountId, TeamDto teamDto) {
        boolean isMember = teamMemberService.isAccountTeamMember(currentAccountId, teamDto.getTeamId());
        return isMember ? teamMemberListingService.retrieveTeamMembersDetailed(teamDto.getTeamId(), PageRequest.of(page, TEAM_MEMBER_PAGE_SIZE)) :
                teamMemberListingService.retrieveTeamMembers(teamDto.getTeamId(), PageRequest.of(page, TEAM_MEMBER_PAGE_SIZE));
    }

    private TeamMemberListingBaseResponse mapValues(Page<TeamMemberDto> teamMemberDtoPage) {
        PageDto pageDto = new PageDto<>(teamMemberDtoPage);
        TeamMemberListingBaseResponse teamMemberListingResponse = new TeamMemberListingBaseResponse();
        teamMemberListingResponse.setTeamMemberDtoPage(pageDto);
        return teamMemberListingResponse;
    }

    private InitializeTeamMemberVo mapValues(String currentAccountId, TeamDto teamDto) {
        InitializeTeamMemberVo initializeTeamMemberVo = new InitializeTeamMemberVo();
        initializeTeamMemberVo.setTeamId(teamDto.getTeamId());
        initializeTeamMemberVo.setAccountId(currentAccountId);
        initializeTeamMemberVo.setRole(TeamAccountRoleType.MEMBER);
        return initializeTeamMemberVo;
    }

    private DeleteTeamMemberVo mapToDeleteTeamMember(String currentAccountId, TeamDto teamDto) {
        DeleteTeamMemberVo deleteTeamMemberVo = new DeleteTeamMemberVo();
        deleteTeamMemberVo.setTeamId(teamDto.getTeamId());
        deleteTeamMemberVo.setAccountId(currentAccountId);
        return deleteTeamMemberVo;
    }
}