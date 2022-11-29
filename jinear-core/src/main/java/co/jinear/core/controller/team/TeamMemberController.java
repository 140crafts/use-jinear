package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamMemberManager;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team/member")
public class TeamMemberController {

    private final TeamMemberManager teamMemberManager;

    @GetMapping("/{teamId}/list")
    @ResponseStatus(HttpStatus.OK)
    public TeamMemberListingResponse retrieveTeamMembers(@PathVariable String teamId,
                                                         @RequestParam(required = false, defaultValue = "0") Integer page) {
        return teamMemberManager.retrieveTeamMembers(teamId, page);
    }

}
