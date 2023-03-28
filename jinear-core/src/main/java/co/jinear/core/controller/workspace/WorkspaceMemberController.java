package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceMemberManager;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import co.jinear.core.model.request.workspace.WorkspaceMemberInvitationRespondRequest;
import co.jinear.core.model.request.workspace.WorkspaceMemberInviteRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceInvitationInfoResponse;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import jakarta.validation.Valid;
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

    @PostMapping("/invite")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse inviteWorkspace(@Valid @RequestBody WorkspaceMemberInviteRequest workspaceMemberInviteRequest) {
        return workspaceMemberManager.invite(workspaceMemberInviteRequest);
    }

    @GetMapping("/invitation-info/{token}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceInvitationInfoResponse retrieveInvitationInfo(@PathVariable String token) {
        return workspaceMemberManager.retrieveInvitationInfo(token);
    }

    @PostMapping("/respond-invitation")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse respondInvitation(@Valid @RequestBody WorkspaceMemberInvitationRespondRequest workspaceMemberInvitationRespondRequest) {
        return workspaceMemberManager.respondInvitation(workspaceMemberInvitationRespondRequest);
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
}
