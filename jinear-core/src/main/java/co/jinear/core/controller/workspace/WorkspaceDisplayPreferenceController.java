package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/display-preferences")
public class WorkspaceDisplayPreferenceController {

    private final WorkspaceManager workspaceManager;

    @PutMapping("/{workspaceId}/set-preferred")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse updatePreferredWorkspace(@PathVariable String workspaceId) {
        return workspaceManager.updatePreferredWorkspace(workspaceId);
    }

    @PutMapping("/{workspaceId}/set-preferred-team/{teamId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse updatePreferredTeam(@PathVariable String workspaceId,
                                            @PathVariable String teamId) {
        return workspaceManager.updatePreferredTeam(teamId);
    }

    @PutMapping("/with-username/{workspaceUsername}/set-preferred/")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse updatePreferredWorkspaceWithUsername(@PathVariable String workspaceUsername) {
        return workspaceManager.updatePreferredWorkspaceWithUsername(workspaceUsername);
    }

    @PutMapping("/with-username/{workspaceUsername}/set-preferred-team/{teamUsername}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse updatePreferredTeamWithUsername(@PathVariable String workspaceUsername,
                                            @PathVariable String teamUsername) {
        return workspaceManager.updatePreferredTeamWithUsername(workspaceUsername,teamUsername);
    }
}