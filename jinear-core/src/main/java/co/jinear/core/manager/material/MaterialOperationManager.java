package co.jinear.core.manager.material;

import co.jinear.core.converter.material.MaterialInitializeFileUploadRequestToVoConverter;
import co.jinear.core.converter.material.MaterialInitializeFolderRequestToVoConverter;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.material.WaitingForUploadMaterialResultDto;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.request.material.MaterialInitializeFileUploadRequest;
import co.jinear.core.model.request.material.MaterialInitializeFolderRequest;
import co.jinear.core.model.request.material.MaterialMoveRequest;
import co.jinear.core.model.request.material.MaterialRenameRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.material.MaterialInitializeFileUploadResponse;
import co.jinear.core.model.vo.material.MaterialFileInitializeVo;
import co.jinear.core.model.vo.material.MaterialFolderInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialOperationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.workspace.WorkspaceMediaLimitQueryService;
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
    private final MaterialRetrieveService materialRetrieveService;
    private final PassiveService passiveService;
    private final WorkspaceMediaLimitQueryService workspaceMediaLimitQueryService;

    public BaseResponse initializeFolder(MaterialInitializeFolderRequest materialInitializeFolderRequest, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Initialize folder has started. currentAccountId: {}", currentAccountId);
        MaterialFolderInitializeVo materialFolderInitializeVo = materialInitializeFolderRequestToVoConverter.convert(materialInitializeFolderRequest, workspaceId, currentAccountId);
        materialOperationService.initialize(materialFolderInitializeVo);
        return new BaseResponse();
    }

    public BaseResponse moveTo(MaterialMoveRequest materialMoveRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialMoveRequest.getMaterialId());
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialChangeAccess(currentAccountId, materialDto);
        log.info("Move material has started. currentAccountId: {}", currentAccountId);
        materialOperationService.moveTo(materialMoveRequest.getMaterialId(), materialMoveRequest.getParentMaterialId());
        return new BaseResponse();
    }

    public BaseResponse rename(MaterialRenameRequest materialRenameRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialRenameRequest.getMaterialId());
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialChangeAccess(currentAccountId, materialDto);
        log.info("Rename material has started. currentAccountId: {}", currentAccountId);
        materialOperationService.rename(materialRenameRequest.getMaterialId(), materialRenameRequest.getNewName());
        return new BaseResponse();
    }

    public MaterialInitializeFileUploadResponse initializeFileUpload(MaterialInitializeFileUploadRequest materialInitializeFileUploadRequest, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        workspaceMediaLimitQueryService.validateWorkspaceStorageLimitNotExceeded(workspaceId, materialInitializeFileUploadRequest.getFileSize());
        if (materialInitializeFileUploadRequest.getParentMaterialId() != null) {
            materialAccessValidationService.validateMaterialReadAccess(currentAccountId, materialInitializeFileUploadRequest.getParentMaterialId());
        }
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

    public BaseResponse deletePermanent(String materialId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialChangeAccess(currentAccountId, materialDto);
        log.info("Delete material has started. currentAccountId: {}", currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        materialOperationService.delete(materialId, passiveId);
        return new BaseResponse();
    }

    public BaseResponse updateAccessType(String materialId, MaterialAccessType materialAccessType) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialChangeAccess(currentAccountId, materialDto);
        log.info("Update access type has started. currentAccountId: {}", currentAccountId);
        materialOperationService.updateAccessType(materialId, materialAccessType, currentAccountId);
        return new BaseResponse();
    }

    private MaterialInitializeFileUploadResponse mapResponse(WaitingForUploadMaterialResultDto waitingForUploadMaterialResultDto) {
        MaterialInitializeFileUploadResponse materialInitializeFileUploadResponse = new MaterialInitializeFileUploadResponse();
        materialInitializeFileUploadResponse.setWaitingForUploadMaterialResultDto(waitingForUploadMaterialResultDto);
        return materialInitializeFileUploadResponse;
    }
}
