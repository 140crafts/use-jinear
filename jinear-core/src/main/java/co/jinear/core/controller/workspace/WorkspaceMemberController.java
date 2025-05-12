package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceMemberManager;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/member")
public class WorkspaceMemberController {

    private final WorkspaceMemberRetrieveManager workspaceMemberRetrieveManager;
    private final WorkspaceMemberManager workspaceMemberManager;

    @GetMapping("/{workspaceId}/list")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceMemberListingBaseResponse retrieveWorkspaceMembers(@PathVariable String workspaceId,
                                                                       @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceMemberRetrieveManager.retrieveWorkspaceMembers(workspaceId, page);
    }

    @PostMapping("/{workspaceId}/join")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse joinWorkspace(@PathVariable String workspaceId) {
        return workspaceMemberManager.joinWorkspace(workspaceId);
    }

    @PostMapping("/{workspaceId}/leave")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse leaveWorkspace(@PathVariable String workspaceId) {
        return workspaceMemberManager.leaveWorkspace(workspaceId);
    }

    @DeleteMapping("/{workspaceId}/kick/{workspaceMemberId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse kick(@PathVariable String workspaceId,
                             @PathVariable String workspaceMemberId) {
        return workspaceMemberManager.kick(workspaceId, workspaceMemberId);
    }

    @GetMapping("/{workspaceId}/transfer-ownership/{toAccountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse transferOwnership(@PathVariable String workspaceId, @PathVariable String toAccountId) {
        return workspaceMemberManager.transferWorkspaceOwnership(workspaceId, toAccountId);
    }
}
