package co.jinear.core.controller.management;

import co.jinear.core.manager.management.AdminWorkspaceManager;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.request.management.AdminWorkspaceInitializeRequest;
import co.jinear.core.model.request.management.AdminWorkspaceMemberAddRequest;
import co.jinear.core.model.request.workspace.WorkspaceTitleUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.management.AdminWorkspaceListingResponse;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceMemberListingBaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/admin/workspace")
public class AdminWorkspaceController {

    private final AdminWorkspaceManager adminWorkspaceManager;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public AdminWorkspaceListingResponse retrieveAllWorkspaces(@RequestParam(required = false, defaultValue = "0") Integer page) {
        return adminWorkspaceManager.retrieveAllWorkspaces(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceBaseResponse initializeWorkspace(@Valid @RequestBody AdminWorkspaceInitializeRequest adminWorkspaceInitializeRequest) {
        return adminWorkspaceManager.initializeWorkspace(adminWorkspaceInitializeRequest);
    }

    @PutMapping("/{workspaceId}/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitle(@PathVariable String workspaceId,
                                    @Valid @RequestBody WorkspaceTitleUpdateRequest workspaceTitleUpdateRequest) {
        return adminWorkspaceManager.updateTitle(workspaceId, workspaceTitleUpdateRequest);
    }

    @PutMapping("/{workspaceId}/tier/{workspaceTier}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTier(@PathVariable String workspaceId,
                                   @PathVariable WorkspaceTier workspaceTier) {
        return adminWorkspaceManager.updateTier(workspaceId, workspaceTier);
    }

    @DeleteMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteWorkspace(@PathVariable String workspaceId) {
        return adminWorkspaceManager.deleteWorkspace(workspaceId);
    }

    @GetMapping("/{workspaceId}/member/list")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceMemberListingBaseResponse retrieveWorkspaceMembers(@PathVariable String workspaceId,
                                                                       @RequestParam(required = false, defaultValue = "0") Integer page) {
        return adminWorkspaceManager.retrieveWorkspaceMembers(workspaceId, page);
    }

    @PostMapping("/{workspaceId}/member")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse addWorkspaceMember(@PathVariable String workspaceId,
                                           @Valid @RequestBody AdminWorkspaceMemberAddRequest adminWorkspaceMemberAddRequest) {
        return adminWorkspaceManager.addWorkspaceMember(workspaceId, adminWorkspaceMemberAddRequest);
    }

    @DeleteMapping("/{workspaceId}/member/{workspaceMemberId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse removeWorkspaceMember(@PathVariable String workspaceId,
                                              @PathVariable String workspaceMemberId) {
        return adminWorkspaceManager.removeWorkspaceMember(workspaceId, workspaceMemberId);
    }
}
