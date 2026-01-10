package co.jinear.core.service.material;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.repository.material.MaterialRepository;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialAccessValidationService {

    private final MaterialRepository materialRepository;
    private final MaterialRetrieveService materialRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final MaterialAccessService materialAccessService;

    public void validateOwnership(String workspaceId, String materialId, String ownerId) {
        log.info("Check ownership has started. workspaceId: {}, materialId: {}, ownerId: {}", workspaceId, materialId, ownerId);
        boolean exists = materialRepository.existsByWorkspaceIdAndMaterialIdAndOwnerIdAndPassiveIdIsNull(workspaceId, materialId, ownerId);
        if (!exists) {
            throw new NoAccessException();
        }
    }

    public void validateMaterialIsInWorkspace(String materialId, String workspaceId) {
        boolean exists = materialRetrieveService.existsByMaterialIdAndWorkspaceId(materialId, workspaceId);
        if (!exists) {
            throw new NoAccessException();
        }
    }

    public void validateMaterialChangeAccess(String accountId, String materialId) {
        log.info("Validate material change access has started. accountId: {}, materialId: {}", accountId, materialId);
        MaterialDto material = materialRetrieveService.retrieve(materialId);
        validateMaterialChangeAccess(accountId, material);
    }

    public void validateMaterialChangeAccess(String accountId, MaterialDto materialDto) {
        log.info("Validate material change access has started. accountId: {}, materialId: {}", accountId, materialDto);
        boolean workspaceAdminOrOwner = workspaceValidator.isWorkspaceAdminOrOwner(accountId, materialDto.getWorkspaceId());
        boolean hasAccess = materialDto.getOwnerId().equals(accountId) || workspaceAdminOrOwner;
        if (!hasAccess) {
            throw new NoAccessException();
        }
    }

    public void validateMaterialReadAccess(String accountId, String materialId) {
        log.info("Validate material read access has started. accountId: {}, materialId: {}", accountId, materialId);
        MaterialDto material = materialRetrieveService.retrieve(materialId);
        validateMaterialReadAccess(accountId, material);
    }

    public void validateMaterialReadAccess(String accountId, MaterialDto materialDto) {
        log.info("Validate material read access has started. accountId: {}, materialId: {}", accountId, materialDto);
        boolean workspaceAdminOrOwner = workspaceValidator.isWorkspaceAdminOrOwner(accountId, materialDto.getWorkspaceId());
        boolean hasMaterialAccess = Boolean.FALSE;
        if (MaterialAccessType.GRAINED.equals(materialDto.getMaterialAccessType())) {
            hasMaterialAccess = materialAccessService.hasAccess(materialDto.getMaterialId(), accountId);
        } else if (MaterialAccessType.WORKSPACE_MEMBERS.equals(materialDto.getMaterialAccessType())) {
            hasMaterialAccess = workspaceValidator.isAccountWorkspaceMember(accountId, materialDto.getWorkspaceId());
        } else if (MaterialAccessType.OWNER_ONLY.equals(materialDto.getMaterialAccessType())) {
            hasMaterialAccess = materialDto.getOwnerId().equals(accountId);
        }
        boolean hasAccess = hasMaterialAccess || workspaceAdminOrOwner;
        if (!hasAccess) {
            throw new NoAccessException();
        }
    }
}
