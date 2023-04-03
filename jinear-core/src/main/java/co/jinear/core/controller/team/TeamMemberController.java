package co.jinear.core.controller.team;

import co.jinear.core.manager.team.TeamMemberManager;
import co.jinear.core.model.request.team.AddTeamMemberRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/team/member")
public class TeamMemberController {

    private final TeamMemberManager teamMemberManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse addTeamMember(@Valid @RequestBody AddTeamMemberRequest addTeamMemberRequest) {
        return teamMemberManager.addTeamMember(addTeamMemberRequest);
    }

    @DeleteMapping("/{teamMemberId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse kickTeamMember(@PathVariable String teamMemberId) {
        return teamMemberManager.kickTeamMember(teamMemberId);
    }

    @GetMapping("/list/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamMemberListingResponse retrieveTeamMembers(@PathVariable String teamId,
                                                         @RequestParam(required = false, defaultValue = "0") Integer page) {
        return teamMemberManager.retrieveTeamMembers(teamId, page);
    }

}
