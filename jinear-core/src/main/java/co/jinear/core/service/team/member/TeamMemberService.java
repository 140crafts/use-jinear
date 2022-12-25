package co.jinear.core.service.team.member;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.entity.team.TeamMember;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.repository.TeamMemberRepository;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.workspace.WorkspaceActivityService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final ModelMapper modelMapper;

    public TeamMemberDto addTeamMember(TeamMemberAddVo teamMemberAddVo) {
        log.info("Add team member has started. teamMemberAddVo: {}", teamMemberAddVo);
        TeamMember teamMember = modelMapper.map(teamMemberAddVo, TeamMember.class);
        assignWorkspaceId(teamMemberAddVo, teamMember);
        TeamMember saved = teamMemberRepository.save(teamMember);
        return modelMapper.map(saved, TeamMemberDto.class);
    }

    public List<TeamMemberDto> addAllFromWorkspace(String teamId) {
        log.info("Add all members from workspace has started for teamId: {}", teamId);
        String workspaceId = retrieveWorkspaceIdFromTeamId(teamId);
        List<WorkspaceMemberDto> workspaceMembers = workspaceMemberListingService.listAllWorkspaceMembers(workspaceId);
        List<TeamMember> teamMembers = workspaceMembers
                .stream()
                .map(workspaceMemberDto -> convertToTeamMember(teamId, workspaceMemberDto))
                .toList();
        List<TeamMemberDto> saved = teamMemberRepository.saveAll(teamMembers)
                .stream()
                .map(tm -> modelMapper.map(tm, TeamMemberDto.class))
                .toList();
        log.info("Add all members from workspace has ended. [{}] members added to teamId: {}", saved.size(), teamId);
        return saved;
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

    private TeamMember convertToTeamMember(String teamId, WorkspaceMemberDto workspaceMemberDto) {
        TeamMember teamMember = new TeamMember();
        teamMember.setAccountId(workspaceMemberDto.getAccountId());
        teamMember.setWorkspaceId(workspaceMemberDto.getWorkspaceId());
        teamMember.setTeamId(teamId);
        return teamMember;
    }
}
