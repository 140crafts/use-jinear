package co.jinear.core.manager.material;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialAccessDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.material.MaterialAccessPaginatedResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialAccessManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final MaterialRetrieveService materialRetrieveService;
    private final MaterialAccessValidationService materialAccessValidationService;
    private final MaterialAccessService materialAccessService;
    private final PassiveService passiveService;

    public MaterialAccessPaginatedResponse accessList(String materialId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialReadAccess(currentAccountId, materialDto);
        Page<MaterialAccessDto> materialAccessDtos = materialAccessService.retrieveAccessList(materialId, page);
        return mapResponse(materialAccessDtos);
    }

    public BaseResponse giveAccess(String materialId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialChangeAccess(currentAccountId, materialDto);
        log.info("Give access has started. currentAccountId: {}", currentAccountId);
        materialAccessService.giveAccess(materialId, accountId);
        return new BaseResponse();
    }

    public BaseResponse revokeAccess(String materialId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialChangeAccess(currentAccountId, materialDto);
        log.info("Revoke access has started. currentAccountId: {}", currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        materialAccessService.revokeAccess(materialId, accountId, passiveId);
        return new BaseResponse();
    }

    private MaterialAccessPaginatedResponse mapResponse(Page<MaterialAccessDto> materialAccessDtos) {
        MaterialAccessPaginatedResponse materialAccessPaginatedResponse = new MaterialAccessPaginatedResponse();
        materialAccessPaginatedResponse.setMaterialAccessDtoPageDto(new PageDto<>(materialAccessDtos));
        return materialAccessPaginatedResponse;
    }
}
