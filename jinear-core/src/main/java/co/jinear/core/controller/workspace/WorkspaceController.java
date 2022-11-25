package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace")
public class WorkspaceController {

    private final WorkspaceManager workspaceManager;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceBaseResponse initializeWorkspace(@Valid @RequestBody WorkspaceInitializeRequest workspaceInitializeRequest) {
        return workspaceManager.initializeWorkspace(workspaceInitializeRequest);
    }
}