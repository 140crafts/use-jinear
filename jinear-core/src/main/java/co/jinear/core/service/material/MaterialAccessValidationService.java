package co.jinear.core.service.material;

import co.jinear.core.exception.NoAccessException;
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
}
