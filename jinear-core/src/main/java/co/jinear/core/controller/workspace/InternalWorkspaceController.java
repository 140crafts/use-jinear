package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.response.workspace.AccountWorkspacesResponse;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/internal/workspace")
public class InternalWorkspaceController {

    private final WorkspaceManager workspaceManager;

    @GetMapping("/{workspaceUsername}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceBaseResponse retrieveWorkspace(@PathVariable String workspaceUsername) {
        return workspaceManager.retrieveWorkspaceWithUsernameInternal(workspaceUsername);
    }

    @GetMapping("/with-id/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceBaseResponse retrieveWorkspaceWithId(@PathVariable String workspaceId) {
        return workspaceManager.retrieveWorkspaceWithIdInternal(workspaceId);
    }

    @GetMapping("/by-account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountWorkspacesResponse retrieveAccountWorkspaces(@PathVariable String accountId) {
        return workspaceManager.retrieveAccountWorkspacesInternal(accountId);
    }
}