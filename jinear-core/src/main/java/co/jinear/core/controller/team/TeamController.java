package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamInitializeManager;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.manager.team.TeamUpdateManager;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team")
public class TeamController {

    private final TeamInitializeManager teamInitializeManager;
    private final TeamRetrieveManager teamRetrieveManager;
    private final TeamUpdateManager teamUpdateManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse initializeTeam(@Valid @RequestBody TeamInitializeRequest teamInitializeRequest) {
        return teamInitializeManager.initializeTeam(teamInitializeRequest);
    }

    @GetMapping("/from-workspace/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamListingResponse retrieveWorkspaceTeams(@PathVariable String workspaceId) {
        return teamRetrieveManager.retrieveWorkspaceTeams(workspaceId);
    }

    @PutMapping("/{teamId}/task-visibility-type/{taskVisibilityType}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTeamTaskVisibilityType(@PathVariable String teamId, @PathVariable TeamTaskVisibilityType taskVisibilityType) {
        return teamUpdateManager.updateTeamTaskVisibilityType(teamId, taskVisibilityType);
    }
}
