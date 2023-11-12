package co.jinear.core.service.team;

import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamUpdateService {

    private final TeamRetrieveService teamRetrieveService;
    private final TeamRepository teamRepository;

    public void updateTeamTaskVisibilityType(String teamId, TeamTaskVisibilityType taskVisibilityType) {
        log.info("Update team task visibility type has started. teamId: {}, taskVisibilityType: {}", teamId, taskVisibilityType);
        Team team = teamRetrieveService.retrieveEntity(teamId);
        team.setTaskVisibility(taskVisibilityType);
        teamRepository.save(team);
    }
}
