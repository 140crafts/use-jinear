package co.jinear.core.service.team;

import co.jinear.core.model.entity.team.Team;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.team.member.TeamMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamDeleteService {

    private final TeamRepository teamRepository;
    private final TeamRetrieveService teamRetrieveService;
    private final TeamMemberService teamMemberService;
    private final PassiveService passiveService;

    @Transactional
    public String deleteTeam(String teamId, String responsibleAccountId) {
        log.info("Delete team has started. teamId: {}, responsibleAccountId: {}", teamId, responsibleAccountId);
        Team team = teamRetrieveService.retrieveEntity(teamId);
        String passiveId = passiveService.createUserActionPassive(responsibleAccountId);
        team.setPassiveId(passiveId);
        teamRepository.save(team);
        teamMemberService.removeAllTeamMembersOfATeam(teamId, passiveId);
        log.info("Delete team has completed. teamId: {}, passiveId: {}", teamId, passiveId);
        return passiveId;
    }

    @Transactional
    public void deleteAllWorkspaceTeams(String workspaceId, String passiveId) {
        log.info("Delete all workspace teams has started. workspaceId: {}, passiveId: {}", workspaceId, passiveId);
        List<Team> teams = teamRepository.findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateAsc(workspaceId);
        teams.forEach(team -> team.setPassiveId(passiveId));
        teamRepository.saveAll(teams);
        teamMemberService.removeAllTeamMembershipsOfAWorkspace(workspaceId, passiveId);
        log.info("Delete all workspace teams has completed. workspaceId: {}, teamCount: {}", workspaceId, teams.size());
    }
}
