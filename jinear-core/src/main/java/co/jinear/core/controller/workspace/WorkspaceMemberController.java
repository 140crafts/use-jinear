package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/member")
public class WorkspaceMemberController {

    private final WorkspaceMemberManager workspaceMemberManager;

    @GetMapping("/{workspaceId}/list")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceMemberListingBaseResponse retrieveWorkspaceMembers(@PathVariable String workspaceId,
                                                                       @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceMemberManager.retrieveWorkspaceMembers(workspaceId, page);
    }

    @PostMapping("/{workspaceUsername}/join")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse joinWorkspace(@PathVariable String workspaceUsername) {
        return workspaceMemberManager.joinWorkspace(workspaceUsername);
    }

    @PostMapping("/{workspaceUsername}/leave")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse leaveWorkspace(@PathVariable String workspaceUsername) {
        return workspaceMemberManager.leaveWorkspace(workspaceUsername);
    }
}
