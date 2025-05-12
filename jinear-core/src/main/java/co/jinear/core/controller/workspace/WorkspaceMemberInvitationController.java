package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceMemberInvitationManager;
import co.jinear.core.model.request.workspace.WorkspaceMemberInvitationRespondRequest;
import co.jinear.core.model.request.workspace.WorkspaceMemberInviteRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceInvitationInfoResponse;
import co.jinear.core.model.response.workspace.WorkspaceInvitationListingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/member/invitation")
public class WorkspaceMemberInvitationController {

    private final WorkspaceMemberInvitationManager workspaceMemberInvitationManager;

    @GetMapping("/list/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceInvitationListingResponse listInvitations(@PathVariable String workspaceId,
                                                              @RequestParam(required = false, defaultValue = "0") Integer page) {
        return workspaceMemberInvitationManager.listInvitations(workspaceId, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse inviteWorkspace(@Valid @RequestBody WorkspaceMemberInviteRequest workspaceMemberInviteRequest) {
        return workspaceMemberInvitationManager.invite(workspaceMemberInviteRequest);
    }

    @DeleteMapping("/{invitationId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteInvitation(@PathVariable String invitationId) {
        return workspaceMemberInvitationManager.delete(invitationId);
    }

    @GetMapping("/info/{token}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceInvitationInfoResponse retrieveInvitationInfo(@PathVariable String token) {
        return workspaceMemberInvitationManager.retrieveInvitationInfo(token);
    }

    @PostMapping("/respond")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse respondInvitation(@Valid @RequestBody WorkspaceMemberInvitationRespondRequest workspaceMemberInvitationRespondRequest) {
        return workspaceMemberInvitationManager.respondInvitation(workspaceMemberInvitationRespondRequest);
    }
}
