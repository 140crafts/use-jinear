package co.jinear.core.service.material;

import co.jinear.core.converter.material.MaterialDtoConverter;
import co.jinear.core.converter.material.MaterialTypeConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.material.PathAwareMaterialDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.material.MaterialPathDto;
import co.jinear.core.model.dto.material.MaterialPathItemDto;
import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.repository.material.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialRetrieveService {

    private final MaterialRepository materialRepository;
    private final MaterialDtoConverter materialDtoConverter;
    private final MaterialTypeConverter materialTypeConverter;

    public MaterialDto retrieve(String materialId) {
        log.info("Retrieve material has started. materialId: {}", materialId);
        return materialRepository.findByMaterialIdAndPassiveIdIsNull(materialId)
                .map(materialDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public MaterialDto retrieve(String materialId, MaterialType materialType) {
        log.info("Retrieve material has started. materialId: {}, materialType: {}", materialId, materialType);
        return materialRepository.findByMaterialIdAndMaterialTypeAndPassiveIdIsNull(materialId, materialType)
                .map(materialDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Material retrieveEntity(String materialId) {
        return materialRepository.findByMaterialIdAndPassiveIdIsNull(materialId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean existsByMaterialIdAndWorkspaceId(String materialId, String workspaceId) {
        log.info("Exists by material id and workspace id has started. materialId: {}, workspaceId: {}", materialId, workspaceId);
        return materialRepository.existsByMaterialIdAndWorkspaceIdAndPassiveIdIsNull(materialId, workspaceId);
    }

    public PathAwareMaterialDto retrievePathAware(String materialId) {
        log.info("Retrieve detailed material has started. materialId: {}", materialId);
        MaterialDto materialDto = retrieve(materialId);
        MaterialPathDto materialPath = getMaterialPath(materialId);
        return materialDtoConverter.convert(materialDto, materialPath);
    }

    private MaterialPathDto getMaterialPath(String materialId) {
        log.info("Get material path has started. materialId: {}", materialId);
        List<Object[]> pathData = materialRepository.findMaterialPath(materialId);

        List<MaterialPathItemDto> path = pathData.stream()
                .map(row -> MaterialPathItemDto.builder()
                        .materialId((String) row[0])
                        .parentMaterialId((String) row[1])
                        .name((String) row[2])
                        .materialType(materialTypeConverter.convertToEntityAttribute((int) row[3]))
                        .build())
                .toList();

        return MaterialPathDto.builder()
                .materialId(materialId)
                .path(path)
                .fullPath(path.stream()
                        .map(MaterialPathItemDto::getName)
                        .collect(Collectors.joining(" / ")))
                .build();
    }
}
