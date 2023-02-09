package co.jinear.core.manager.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamRetrieveManager {

    private final SessionInfoService sessionInfoService;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceValidator workspaceValidator;

    public TeamListingResponse retrieveWorkspaceTeams(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("List workspace teams has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        List<TeamDto> teamDtoList = teamRetrieveService.listWorkspaceTeamsByAccountWorkspaceRole(currentAccountId, workspaceId);
        log.info("List workspace teams has finished.");
        return mapResponse(teamDtoList);
    }

    private TeamListingResponse mapResponse(List<TeamDto> teamDtoList) {
        TeamListingResponse teamListingResponse = new TeamListingResponse();
        teamListingResponse.setTeamDtoList(teamDtoList);
        return teamListingResponse;
    }
}
