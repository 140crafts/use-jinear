package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceMediaManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceMediaLimitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/media")
public class WorkspaceMediaController {

    private final WorkspaceMediaManager workspaceMediaManager;

    @PostMapping(value = "/{workspaceId}/profile-picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateWorkspaceProfilePicture(@PathVariable String workspaceId,
                                                      @RequestParam("file") MultipartFile file) {
        return workspaceMediaManager.changeWorkspaceProfilePicture(file, workspaceId);
    }

    @GetMapping(value = "/{workspaceId}/limits")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceMediaLimitResponse retrieveWorkspaceMediaLimits(@PathVariable String workspaceId) {
        return workspaceMediaManager.retrieveLimits(workspaceId);
    }
}
