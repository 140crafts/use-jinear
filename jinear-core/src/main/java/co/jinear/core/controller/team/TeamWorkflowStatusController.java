package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.model.request.team.InitializeTeamWorkflowStatusRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamWorkflowStatusListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team/workflow-status")
public class TeamWorkflowStatusController {

    private final TeamWorkflowStatusManager teamWorkflowStatusManager;

    @GetMapping("/{teamId}/list")
    @ResponseStatus(HttpStatus.OK)
    public TeamWorkflowStatusListingResponse retrieveAllFromTeam(@PathVariable String teamId) {
        return teamWorkflowStatusManager.retrieveAllFromTeam(teamId);
    }

    @PostMapping("/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse retrieveAllFromTeam(@PathVariable String teamId,
                                            @Valid @RequestBody InitializeTeamWorkflowStatusRequest initializeTeamWorkflowStatusRequest) {
        return teamWorkflowStatusManager.initializeTeamWorkflowStatus(teamId, initializeTeamWorkflowStatusRequest);
    }

    @DeleteMapping("/{teamId}/{teamWorkflowStatusId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse removeTeamWorkflowStatus(@PathVariable String teamId,
                                                 @PathVariable String teamWorkflowStatusId) {
        return teamWorkflowStatusManager.removeTeamWorkflowStatus(teamId, teamWorkflowStatusId);
    }

    @PutMapping("/{teamId}/change-order/{teamWorkflowStatusId}/with/{replaceWithTeamWorkflowStatusId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse changeOrder(@PathVariable String teamId,
                                    @PathVariable String teamWorkflowStatusId,
                                    @PathVariable String replaceWithTeamWorkflowStatusId) {
        return teamWorkflowStatusManager.changeOrder(teamId, teamWorkflowStatusId, replaceWithTeamWorkflowStatusId);
    }
}
