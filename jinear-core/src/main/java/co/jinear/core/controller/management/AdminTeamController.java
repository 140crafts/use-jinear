package co.jinear.core.controller.management;

import co.jinear.core.manager.management.AdminTeamManager;
import co.jinear.core.model.request.management.AdminTeamMemberAddRequest;
import co.jinear.core.model.request.management.AdminTeamRenameRequest;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import co.jinear.core.model.response.team.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/admin/team")
public class AdminTeamController {

    private final AdminTeamManager adminTeamManager;

    @GetMapping("/list/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamListingResponse retrieveWorkspaceTeams(@PathVariable String workspaceId) {
        return adminTeamManager.retrieveWorkspaceTeams(workspaceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse initializeTeam(@Valid @RequestBody TeamInitializeRequest teamInitializeRequest) {
        return adminTeamManager.initializeTeam(teamInitializeRequest);
    }

    @PutMapping("/{teamId}/name")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse renameTeam(@PathVariable String teamId,
                                   @Valid @RequestBody AdminTeamRenameRequest adminTeamRenameRequest) {
        return adminTeamManager.renameTeam(teamId, adminTeamRenameRequest);
    }

    @DeleteMapping("/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTeam(@PathVariable String teamId) {
        return adminTeamManager.deleteTeam(teamId);
    }

    @GetMapping("/{teamId}/member/list")
    @ResponseStatus(HttpStatus.OK)
    public TeamMemberListingResponse retrieveTeamMembers(@PathVariable String teamId,
                                                         @RequestParam(required = false, defaultValue = "0") Integer page) {
        return adminTeamManager.retrieveTeamMembers(teamId, page);
    }

    @PostMapping("/{teamId}/member")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse addTeamMember(@PathVariable String teamId,
                                      @Valid @RequestBody AdminTeamMemberAddRequest adminTeamMemberAddRequest) {
        return adminTeamManager.addTeamMember(teamId, adminTeamMemberAddRequest);
    }

    @DeleteMapping("/member/{teamMemberId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse removeTeamMember(@PathVariable String teamMemberId) {
        return adminTeamManager.removeTeamMember(teamMemberId);
    }
}
