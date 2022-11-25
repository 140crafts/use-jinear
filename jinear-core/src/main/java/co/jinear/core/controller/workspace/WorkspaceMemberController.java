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

    @GetMapping("/{workspaceUsername}/list")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceMemberListingBaseResponse retrieveWorkspace(@PathVariable String workspaceUsername,
                                                                @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceMemberManager.retrieveWorkspaceMembers(workspaceUsername, page);
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
