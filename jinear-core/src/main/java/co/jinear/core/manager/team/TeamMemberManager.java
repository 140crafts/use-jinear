package co.jinear.core.manager.team;

import co.jinear.core.converter.team.TeamMemberConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamStateType;
import co.jinear.core.model.request.team.AddTeamMemberRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import co.jinear.core.model.response.team.TeamMembershipsResponse;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.member.TeamMemberListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberManager {

    private final TeamMemberListingService teamMemberListingService;
    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;
    private final TeamRetrieveService teamRetrieveService;
    private final TeamMemberService teamMemberService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TeamMemberConverter teamMemberConverter;
    private final WorkspaceValidator workspaceValidator;
    private final PassiveService passiveService;

    public BaseResponse addTeamMember(AddTeamMemberRequest addTeamMemberRequest) {
        String accountId = sessionInfoService.currentAccountId();
        TeamDto teamDto = teamRetrieveService.retrieveTeam(addTeamMemberRequest.getTeamId());
        teamAccessValidator.validateTeamAdminOrWorkspaceAdminOrWorkspaceOwner(accountId, teamDto.getWorkspaceId(), teamDto.getTeamId());
        validateTeamExistenceAndState(teamDto.getWorkspaceId(), addTeamMemberRequest);
        TeamMemberAddVo teamMemberAddVo = teamMemberConverter.map(addTeamMemberRequest);
        teamMemberService.addTeamMember(teamMemberAddVo);
        //todo workspace activity
        return new BaseResponse();
    }

    public BaseResponse kickTeamMember(String teamMemberId) {
        String accountId = sessionInfoService.currentAccountId();
        TeamMemberDto teamMemberDto = teamMemberRetrieveService.retrieve(teamMemberId);
        teamAccessValidator.validateTeamAdminOrWorkspaceAdminOrWorkspaceOwner(accountId, teamMemberDto.getWorkspaceId(), teamMemberDto.getTeamId());
        log.info("Kick team member has started. currentAccountId: {}", accountId);
        String passiveId = teamMemberService.removeTeamMember(teamMemberId);
        passiveService.assignOwnership(passiveId, accountId);
        //todo workspace activity
        return new BaseResponse();
    }

    public TeamMemberListingResponse retrieveTeamMembers(String teamId, int page) {
        String accountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(accountId, teamId);
        log.info("Retrieve team members has started. teamId: {}, page: {}, accountId: {}", teamId, page, accountId);
        Page<TeamMemberDto> teamMemberDtoPage = teamMemberListingService.retrieveTeamMembers(teamId, page);
        return mapToResponse(teamMemberDtoPage);
    }

    public TeamMembershipsResponse retrieveMemberships(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        log.info("Retrieve memberships has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        List<TeamMemberDto> teamMemberDtos = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(accountId, workspaceId);
        return mapResponse(teamMemberDtos);
    }

    private void validateTeamExistenceAndState(String workspaceId, AddTeamMemberRequest addTeamMemberRequest) {
        boolean existsAndActive = teamRetrieveService.checkTeamExistenceWithState(workspaceId, addTeamMemberRequest.getTeamId(), TeamStateType.ACTIVE);
        if (Boolean.FALSE.equals(existsAndActive)) {
            throw new BusinessException();
        }
    }

    private TeamMembershipsResponse mapResponse(List<TeamMemberDto> teamMemberDtos) {
        TeamMembershipsResponse teamMembershipsResponse = new TeamMembershipsResponse();
        teamMembershipsResponse.setTeamMemberDtoList(teamMemberDtos);
        return teamMembershipsResponse;
    }

    private TeamMemberListingResponse mapToResponse(Page<TeamMemberDto> teamMemberDtoPage) {
        TeamMemberListingResponse teamMemberListingResponse = new TeamMemberListingResponse();
        teamMemberListingResponse.setTeamMemberDtoList(new PageDto<>(teamMemberDtoPage));
        return teamMemberListingResponse;
    }
}
