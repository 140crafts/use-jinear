package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamManager;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.team.TeamBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team")
public class TeamController {

    private final TeamManager teamManager;

    @GetMapping("/{teamUsername}")
    @ResponseStatus(HttpStatus.OK)
    public TeamBaseResponse retrieveTeam(@PathVariable String teamUsername) {
        return teamManager.retrieveTeamWithUsername(teamUsername);
    }

    @GetMapping("/with-id/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamBaseResponse retrieveTeamWithId(@PathVariable String teamId) {
        return teamManager.retrieveTeamWithId(teamId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamBaseResponse initializeTeam(@Valid @RequestBody TeamInitializeRequest teamInitializeRequest) {
        return teamManager.initializeTeam(teamInitializeRequest);
    }
}