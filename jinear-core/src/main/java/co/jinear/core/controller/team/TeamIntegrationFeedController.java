package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamIntegrationFeedManager;
import co.jinear.core.model.response.team.TeamIntegrationFeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team/integration/feed")
public class TeamIntegrationFeedController {

    private final TeamIntegrationFeedManager teamIntegrationFeedManager;

    @GetMapping("/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamIntegrationFeedResponse retrieveTeamIntegrationFeed(@PathVariable String teamId,
                                                              @RequestParam(required = false, defaultValue = "0") Integer page) {
        return teamIntegrationFeedManager.retrieveFeed(teamId, page);
    }
}
