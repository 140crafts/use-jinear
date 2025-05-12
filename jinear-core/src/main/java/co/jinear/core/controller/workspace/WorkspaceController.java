package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import co.jinear.core.model.request.workspace.WorkspaceTitleUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace")
public class WorkspaceController {

    private final WorkspaceManager workspaceManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceBaseResponse initializeWorkspace(@RequestParam(value = "logo", required = false) MultipartFile logo,
                                                     @Valid WorkspaceInitializeRequest workspaceInitializeRequest) {
        return workspaceManager.initializeWorkspace(logo, workspaceInitializeRequest);
    }

    @GetMapping("/{workspaceUsername}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceBaseResponse retrieveWorkspace(@PathVariable String workspaceUsername) {
        return workspaceManager.retrieveWorkspaceWithUsername(workspaceUsername);
    }

    @GetMapping("/with-id/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceBaseResponse retrieveWorkspaceWithId(@PathVariable String workspaceId) {
        return workspaceManager.retrieveWorkspaceWithId(workspaceId);
    }

    @PutMapping("/update/{workspaceId}/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitle(@PathVariable String workspaceId,
                                    @Valid @RequestBody WorkspaceTitleUpdateRequest workspaceTitleUpdateRequest) {
        return workspaceManager.updateTitle(workspaceId, workspaceTitleUpdateRequest);
    }
}