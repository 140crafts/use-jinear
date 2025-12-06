package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialOperationManager;
import co.jinear.core.model.request.material.MaterialInitializeFileUploadRequest;
import co.jinear.core.model.request.material.MaterialInitializeFolderRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.material.MaterialInitializeFileUploadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/material/operation")
public class MaterialOperationController {

    private final MaterialOperationManager materialOperationManager;

    @PostMapping("/workspace/{workspaceId}/folder")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeFolder(@PathVariable String workspaceId,
                                         @Valid @RequestBody MaterialInitializeFolderRequest materialInitializeFolderRequest) {
        return materialOperationManager.initializeFolder(materialInitializeFolderRequest, workspaceId);
    }

    @PostMapping("/workspace/{workspaceId}/file/upload-url")
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialInitializeFileUploadResponse initializeFileUpload(@PathVariable String workspaceId,
                                                                     @Valid @RequestBody MaterialInitializeFileUploadRequest materialInitializeFileUploadRequest) {
        return materialOperationManager.initializeFileUpload(materialInitializeFileUploadRequest, workspaceId);
    }

    @PostMapping("/workspace/{workspaceId}/file/upload-url/notify/{materialId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse notifyMaterialUploaded(@PathVariable String workspaceId,
                                               @PathVariable String materialId) {
        return materialOperationManager.notifyFileUploadComplete(workspaceId, materialId);
    }

}
