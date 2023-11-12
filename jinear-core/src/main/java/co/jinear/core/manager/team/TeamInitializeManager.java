package co.jinear.core.manager.team;

import co.jinear.core.converter.team.TeamInitializeVoConverter;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.team.TeamResponse;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamInitializeService;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.VISIBLE_TO_ALL_TEAM_MEMBERS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamInitializeManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final WorkspaceTierValidator workspaceTierValidator;
    private final TeamInitializeService teamInitializeService;
    private final TeamInitializeVoConverter teamInitializeVoConverter;

    public TeamResponse initializeTeam(TeamInitializeRequest teamInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String workspaceId = teamInitializeRequest.getWorkspaceId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        validateTeamVisibilityTypeAccess(teamInitializeRequest, workspaceId);
        log.info("Initialize team has started. currentAccountId: {}", currentAccountId);
        TeamInitializeVo teamInitializeVo = teamInitializeVoConverter.map(teamInitializeRequest, currentAccountId);
        TeamDto teamDto = teamInitializeService.initializeTeam(teamInitializeVo);
        log.info("Initialize team has finished.");
        return mapResponse(teamDto);
    }

    private void validateTeamVisibilityTypeAccess(TeamInitializeRequest teamInitializeRequest, String workspaceId) {
        TeamTaskVisibilityType taskVisibility = teamInitializeRequest.getTaskVisibility();
        if (VISIBLE_TO_ALL_TEAM_MEMBERS.equals(taskVisibility)) {
            workspaceTierValidator.validateWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(workspaceId);
        }
    }

    private TeamResponse mapResponse(TeamDto teamDto) {
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setTeamDto(teamDto);
        return teamResponse;
    }
}
