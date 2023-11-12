package co.jinear.core.manager.team;

import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamUpdateService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamUpdateManager {

    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;
    private final WorkspaceTierValidator workspaceTierValidator;
    private final TeamUpdateService teamUpdateService;

    public BaseResponse updateTeamTaskVisibilityType(String teamId, TeamTaskVisibilityType taskVisibilityType) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAdminAccess(currentAccountId, teamId);
        workspaceTierValidator.validateTeamsWorkspaceHasAdvancedTeamTaskVisibilityTypesAccess(teamId);
        log.info("Update team task visibility type has started. currentAccountId: {}", currentAccountId);
        teamUpdateService.updateTeamTaskVisibilityType(teamId, taskVisibilityType);
        return new BaseResponse();
    }
}
