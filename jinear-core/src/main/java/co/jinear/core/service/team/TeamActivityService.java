package co.jinear.core.service.team;

import co.jinear.core.model.entity.team.TeamActivity;
import co.jinear.core.model.vo.team.TeamActivityCreateVo;
import co.jinear.core.repository.TeamActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamActivityService {

    private final TeamActivityRepository teamActivityRepository;

    public void createTeamActivity(TeamActivityCreateVo teamActivityCreateVo) {
        log.info("Create team activity has started. teamActivityCreateVo: {}", teamActivityCreateVo);
        TeamActivity teamActivity = new TeamActivity();
        teamActivity.setTeamId(teamActivityCreateVo.getTeamId());
        teamActivity.setAccountId(teamActivityCreateVo.getAccountId());
        teamActivity.setRelatedObjectId(teamActivityCreateVo.getRelatedObjectId());
        teamActivity.setType(teamActivityCreateVo.getType());
        teamActivityRepository.save(teamActivity);
    }
}
