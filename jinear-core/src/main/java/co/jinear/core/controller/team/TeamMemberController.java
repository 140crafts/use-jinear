package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamMemberListingBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team/member")
public class TeamMemberController {

    private final TeamMemberManager teamMemberManager;

    @GetMapping("/{teamUsername}/list")
    @ResponseStatus(HttpStatus.OK)
    public TeamMemberListingBaseResponse retrieveTeam(@PathVariable String teamUsername,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page) {
        return teamMemberManager.retrieveTeamMembers(teamUsername, page);
    }

    @PostMapping("/{teamUsername}/join")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse joinTeam(@PathVariable String teamUsername) {
        return teamMemberManager.joinTeam(teamUsername);
    }

    @PostMapping("/{teamUsername}/leave")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse leaveTeam(@PathVariable String teamUsername) {
        return teamMemberManager.leaveTeam(teamUsername);
    }
}
