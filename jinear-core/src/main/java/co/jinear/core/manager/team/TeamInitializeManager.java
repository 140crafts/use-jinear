package co.jinear.core.manager.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.team.TeamResponse;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamInitializeService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamInitializeManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamInitializeService teamInitializeService;
    private final ModelMapper modelMapper;

    public TeamResponse initializeTeam(TeamInitializeRequest teamInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, teamInitializeRequest.getWorkspaceId());
        log.info("Initialize team has started. currentAccountId: {}", currentAccountId);
        TeamInitializeVo teamInitializeVo = modelMapper.map(teamInitializeRequest, TeamInitializeVo.class);
        teamInitializeVo.setInitializedBy(currentAccountId);
        TeamDto teamDto = teamInitializeService.initializeTeam(teamInitializeVo);
        log.info("Initialize team has finished.");
        return mapResponse(teamDto);
    }

    private TeamResponse mapResponse(TeamDto teamDto) {
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setTeamDto(teamDto);
        return teamResponse;
    }
}
