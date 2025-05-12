package co.jinear.core.service.team.member;

import co.jinear.core.converter.team.TeamMemberConverter;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.entity.team.TeamMember;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import co.jinear.core.repository.TeamMemberRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final TeamMemberConverter teamMemberConverter;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final PassiveService passiveService;

    public TeamMemberDto addTeamMember(TeamMemberAddVo teamMemberAddVo) {
        log.info("Add team member has started. teamMemberAddVo: {}", teamMemberAddVo);
        TeamMember teamMember = teamMemberConverter.map(teamMemberAddVo);
        assignWorkspaceId(teamMemberAddVo, teamMember);
        TeamMember saved = teamMemberRepository.saveAndFlush(teamMember);
        return teamMemberConverter.map(saved);
    }

    public String removeTeamMember(String teamMemberId) {
        log.info("Remove team member has started. teamMemberId: {}", teamMemberId);
        TeamMember teamMember = teamMemberRetrieveService.retrieveEntity(teamMemberId);
        String passiveId = passiveService.createUserActionPassive();
        teamMember.setPassiveId(passiveId);
        teamMemberRepository.save(teamMember);
        log.info("Remove team member has completed. teamMemberId: {}, passiveId: {}", teamMemberId, passiveId);
        return passiveId;
    }

    public List<TeamMemberDto> addAllFromWorkspace(String teamId, List<String> excludeAccountIds) {
        log.info("Add all members from workspace has started for teamId: {}", teamId);
        String workspaceId = retrieveWorkspaceIdFromTeamId(teamId);
        List<WorkspaceMemberDto> workspaceMembers = workspaceMemberListingService.listAllWorkspaceMembers(workspaceId);
        List<TeamMember> teamMembers = workspaceMembers
                .stream()
                .filter(workspaceMemberDto -> isWorkspaceMemberIsInExcludedAccountIds(excludeAccountIds, workspaceMemberDto))
                .map(workspaceMemberDto -> convertToTeamMember(teamId, workspaceMemberDto))
                .toList();
        List<TeamMemberDto> saved = teamMemberRepository.saveAll(teamMembers)
                .stream()
                .map(teamMemberConverter::map)
                .toList();
        log.info("Add all members from workspace has ended. [{}] members added to teamId: {}", saved.size(), teamId);
        return saved;
    }

    public void removeAllTeamMembershipsOfAnAccount(String accountId, String passiveId) {
        log.info("Remove all team memberships of an account has started. accountId: {}, passiveId: {}", accountId, passiveId);
        List<TeamMember> teamMemberships = teamMemberRepository.findAllByAccountIdAndPassiveIdIsNull(accountId);
        teamMemberships.forEach(teamMember -> teamMember.setPassiveId(passiveId));
        teamMemberRepository.saveAll(teamMemberships);
    }

    private void assignWorkspaceId(TeamMemberAddVo teamMemberAddVo, TeamMember teamMember) {
        String workspaceId = retrieveWorkspaceIdFromTeamId(teamMemberAddVo.getTeamId());
        teamMember.setWorkspaceId(workspaceId);
    }

    private String retrieveWorkspaceIdFromTeamId(String teamId) {
        log.info("Retrieve workspaceId from teamId has started. teamId: {}", teamId);
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        String workspaceId = teamDto.getWorkspaceId();
        log.info("Retrieve workspaceId from teamId has ended. teamId: {}, workspaceId: {}", teamId, workspaceId);
        return workspaceId;
    }

    private boolean isWorkspaceMemberIsInExcludedAccountIds(List<String> excludeAccountIds, WorkspaceMemberDto workspaceMemberDto) {
        return !excludeAccountIds.contains(workspaceMemberDto.getAccountId());
    }

    private TeamMember convertToTeamMember(String teamId, WorkspaceMemberDto workspaceMemberDto) {
        TeamMember teamMember = new TeamMember();
        teamMember.setAccountId(workspaceMemberDto.getAccountId());
        teamMember.setWorkspaceId(workspaceMemberDto.getWorkspaceId());
        teamMember.setTeamId(teamId);
        return teamMember;
    }
}
