package co.jinear.core.manager.material;

import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.response.material.MaterialRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialManager {

    private final MaterialRetrieveService materialRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final MaterialAccessValidationService materialAccessValidationService;

    public MaterialRetrieveResponse retrieve(String materialId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve material has started. currentAccountId: {}", currentAccountId);
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        materialAccessValidationService.validateMaterialReadAccess(currentAccountId, materialDto);
        return mapResponse(materialDto);
    }

    private MaterialRetrieveResponse mapResponse(MaterialDto materialDto) {
        MaterialRetrieveResponse materialRetrieveResponse = new MaterialRetrieveResponse();
        materialRetrieveResponse.setMaterialDto(materialDto);
        return materialRetrieveResponse;
    }
}
