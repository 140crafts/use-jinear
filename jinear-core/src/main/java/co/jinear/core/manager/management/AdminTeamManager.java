package co.jinear.core.manager.management;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.request.management.AdminTeamMemberAddRequest;
import co.jinear.core.model.request.management.AdminTeamRenameRequest;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import co.jinear.core.model.response.team.TeamResponse;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.team.TeamDeleteService;
import co.jinear.core.service.team.TeamInitializeService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.TeamUpdateService;
import co.jinear.core.service.team.member.TeamMemberListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.converter.team.TeamInitializeVoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTeamManager {

    private final SessionInfoService sessionInfoService;
    private final TeamRetrieveService teamRetrieveService;
    private final TeamInitializeService teamInitializeService;
    private final TeamInitializeVoConverter teamInitializeVoConverter;
    private final TeamUpdateService teamUpdateService;
    private final TeamDeleteService teamDeleteService;
    private final TeamMemberListingService teamMemberListingService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TeamMemberService teamMemberService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final AccountRetrieveService accountRetrieveService;

    public TeamListingResponse retrieveWorkspaceTeams(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin retrieve workspace teams has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        List<TeamDto> teamDtoList = teamRetrieveService.retrieveWorkspaceTeams(workspaceId);
        TeamListingResponse teamListingResponse = new TeamListingResponse();
        teamListingResponse.setTeamDtoList(teamDtoList);
        return teamListingResponse;
    }

    public TeamResponse initializeTeam(TeamInitializeRequest request) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin initialize team has started. accountId: {}, request: {}", accountId, request);
        workspaceRetrieveService.retrieveWorkspaceWithId(request.getWorkspaceId());
//        String workspaceOwnerAccountId = workspaceMemberRetrieveService.retrieveWorkspaceOwnerAccountId(request.getWorkspaceId());
        TeamInitializeVo teamInitializeVo = teamInitializeVoConverter.map(request, accountId);
        TeamDto teamDto = teamInitializeService.initializeTeam(teamInitializeVo);
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setTeamDto(teamDto);
        return teamResponse;
    }

    public BaseResponse renameTeam(String teamId, AdminTeamRenameRequest request) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin rename team has started. accountId: {}, teamId: {}, request: {}", accountId, teamId, request);
        teamUpdateService.updateTeamName(teamId, request.getName());
        return new BaseResponse();
    }

    public BaseResponse deleteTeam(String teamId) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin delete team has started. accountId: {}, teamId: {}", accountId, teamId);
        teamDeleteService.deleteTeam(teamId, accountId);
        return new BaseResponse();
    }

    public TeamMemberListingResponse retrieveTeamMembers(String teamId, Integer page) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin retrieve team members has started. accountId: {}, teamId: {}, page: {}", accountId, teamId, page);
        teamRetrieveService.retrieveTeam(teamId);
        Page<TeamMemberDto> teamMemberDtoPage = teamMemberListingService.retrieveTeamMembers(teamId, page);
        TeamMemberListingResponse teamMemberListingResponse = new TeamMemberListingResponse();
        teamMemberListingResponse.setTeamMemberDtoList(new PageDto<>(teamMemberDtoPage));
        return teamMemberListingResponse;
    }

    public BaseResponse addTeamMember(String teamId, AdminTeamMemberAddRequest request) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin add team member has started. accountId: {}, teamId: {}, request: {}", accountId, teamId, request);
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        accountRetrieveService.retrieve(request.getAccountId());
        workspaceMemberService.validateAccountWorkspaceMember(request.getAccountId(), teamDto.getWorkspaceId());
        validateAccountIsNotTeamMember(request.getAccountId(), teamId);
        TeamMemberAddVo teamMemberAddVo = TeamMemberAddVo.builder()
                .accountId(request.getAccountId())
                .teamId(teamId)
                .role(request.getRole())
                .build();
        teamMemberService.addTeamMember(teamMemberAddVo);
        return new BaseResponse();
    }

    public BaseResponse removeTeamMember(String teamMemberId) {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Admin remove team member has started. accountId: {}, teamMemberId: {}", accountId, teamMemberId);
        teamMemberService.removeTeamMember(teamMemberId);
        return new BaseResponse();
    }

    private void validateAccountIsNotTeamMember(String accountId, String teamId) {
        if (teamMemberRetrieveService.isAccountTeamMember(accountId, teamId)) {
            throw new BusinessException("workspace.team.account-already-member");
        }
    }
}
