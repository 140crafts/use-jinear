package co.jinear.core.service.team;

import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.team.TeamStateType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.task.TaskFtsRefreshService;
import co.jinear.core.validator.team.TeamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamUpdateService {

    private final TeamRetrieveService teamRetrieveService;
    private final TeamRepository teamRepository;
    private final TaskFtsRefreshService taskFtsRefreshService;
    private final TeamValidator teamValidator;

    public void updateTeamTaskVisibilityType(String teamId, TeamTaskVisibilityType taskVisibilityType) {
        log.info("Update team task visibility type has started. teamId: {}, taskVisibilityType: {}", teamId, taskVisibilityType);
        Team team = teamRetrieveService.retrieveEntity(teamId);
        team.setTaskVisibility(taskVisibilityType);
        teamRepository.save(team);
        taskFtsRefreshService.markDirty();
    }

    public void updateTeamName(String teamId, String name) {
        log.info("Update team name has started. teamId: {}, name: {}", teamId, name);
        Team team = teamRetrieveService.retrieveEntity(teamId);
        if (team.getName().equals(name)) {
            return;
        }
        teamValidator.validateTeamNameIsNotUsedInWorkspace(name, team.getWorkspaceId());
        team.setName(name);
        teamRepository.save(team);
        taskFtsRefreshService.markDirty();
    }

    public void updateTeamState(String teamId, TeamStateType teamState) {
        log.info("Update team state has started. teamId: {}, teamState: {}", teamId, teamState);
        Team team = teamRetrieveService.retrieveEntity(teamId);
        team.setTeamState(teamState);
        teamRepository.save(team);
    }
}
