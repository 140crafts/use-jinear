package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialOperationManager;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.request.material.MaterialInitializeFileUploadRequest;
import co.jinear.core.model.request.material.MaterialInitializeFolderRequest;
import co.jinear.core.model.request.material.MaterialMoveRequest;
import co.jinear.core.model.request.material.MaterialRenameRequest;
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

    @PutMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse moveTo(@Valid @RequestBody MaterialMoveRequest materialMoveRequest) {
        return materialOperationManager.moveTo(materialMoveRequest);
    }

    @PutMapping("/rename")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse rename(@Valid @RequestBody MaterialRenameRequest materialRenameRequest) {
        return materialOperationManager.rename(materialRenameRequest);
    }

    @DeleteMapping("/{materialId}/permanent")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deletePermanent(@PathVariable String materialId) {
        return materialOperationManager.deletePermanent(materialId);
    }

    @PutMapping("/{materialId}/access-type/{accessType}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateAccessType(@PathVariable String materialId,
                                         @PathVariable MaterialAccessType accessType) {
        return materialOperationManager.updateAccessType(materialId, accessType);
    }

    @PostMapping("/workspace/{workspaceId}/file/upload-url")
    @ResponseStatus(HttpStatus.OK)
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
