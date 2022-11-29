package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamInitializeManager;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team")
public class TeamController {

    private final TeamInitializeManager teamInitializeManager;
    private final TeamRetrieveManager teamRetrieveManager;

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

}
