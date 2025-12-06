package co.jinear.core.service.material;

import co.jinear.core.converter.material.MaterialDtoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.entity.material.Material;
import co.jinear.core.repository.material.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialRetrieveService {

    private final MaterialRepository materialRepository;
    private final MaterialDtoConverter materialDtoConverter;

    public MaterialDto retrieve(String materialId) {
        return materialRepository.findByMaterialIdAndPassiveIdIsNull(materialId)
                .map(materialDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Material retrieveEntity(String materialId) {
        return materialRepository.findByMaterialIdAndPassiveIdIsNull(materialId)
                .orElseThrow(NotFoundException::new);
    }

    public void validateOwnership(String workspaceId, String materialId, String ownerId) {
        log.info("Check ownership has started. workspaceId: {}, materialId: {}, ownerId: {}", workspaceId, materialId, ownerId);
        boolean exists = materialRepository.existsByWorkspaceIdAndMaterialIdAndOwnerIdAndPassiveIdIsNull(workspaceId, materialId, ownerId);
        if (!exists) {
            throw new NoAccessException();
        }
    }
}
