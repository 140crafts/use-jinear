package co.jinear.core.manager.material;

import co.jinear.core.converter.material.MaterialInitializeFileUploadRequestToVoConverter;
import co.jinear.core.converter.material.MaterialInitializeFolderRequestToVoConverter;
import co.jinear.core.model.dto.material.WaitingForUploadMaterialResultDto;
import co.jinear.core.model.request.material.MaterialInitializeFileUploadRequest;
import co.jinear.core.model.request.material.MaterialInitializeFolderRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.material.MaterialInitializeFileUploadResponse;
import co.jinear.core.model.vo.material.MaterialFileInitializeVo;
import co.jinear.core.model.vo.material.MaterialFolderInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialOperationService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialOperationManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final MaterialOperationService materialOperationService;
    private final MaterialAccessValidationService materialAccessValidationService;
    private final MaterialInitializeFolderRequestToVoConverter materialInitializeFolderRequestToVoConverter;
    private final MaterialInitializeFileUploadRequestToVoConverter materialInitializeFileUploadRequestToVoConverter;

    public BaseResponse initializeFolder(MaterialInitializeFolderRequest materialInitializeFolderRequest, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Initialize folder has started. currentAccountId: {}", currentAccountId);
        MaterialFolderInitializeVo materialFolderInitializeVo = materialInitializeFolderRequestToVoConverter.convert(materialInitializeFolderRequest, workspaceId, currentAccountId);
        materialOperationService.initialize(materialFolderInitializeVo);
        return new BaseResponse();
    }

    public MaterialInitializeFileUploadResponse initializeFileUpload(MaterialInitializeFileUploadRequest materialInitializeFileUploadRequest, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Initialize file upload has started. currentAccountId: {}", currentAccountId);
        MaterialFileInitializeVo materialFileInitializeVo = materialInitializeFileUploadRequestToVoConverter.convert(materialInitializeFileUploadRequest, workspaceId, currentAccountId);
        WaitingForUploadMaterialResultDto waitingForUploadMaterialResultDto = materialOperationService.initialize(materialFileInitializeVo);
        return mapResponse(waitingForUploadMaterialResultDto);
    }

    public BaseResponse notifyFileUploadComplete(String workspaceId, String materialId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        materialAccessValidationService.validateOwnership(workspaceId, materialId, currentAccountId);
        log.info("Notify file upload complete has started. currentAccountId: {}", currentAccountId);
        materialOperationService.notifyUploadComplete(materialId);
        return new BaseResponse();
    }

    private MaterialInitializeFileUploadResponse mapResponse(WaitingForUploadMaterialResultDto waitingForUploadMaterialResultDto) {
        MaterialInitializeFileUploadResponse materialInitializeFileUploadResponse = new MaterialInitializeFileUploadResponse();
        materialInitializeFileUploadResponse.setWaitingForUploadMaterialResultDto(waitingForUploadMaterialResultDto);
        return materialInitializeFileUploadResponse;
    }
}
