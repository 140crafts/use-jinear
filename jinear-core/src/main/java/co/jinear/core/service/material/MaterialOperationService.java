package co.jinear.core.service.material;

import co.jinear.core.converter.material.MaterialEntityConverter;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.material.WaitingForUploadMaterialResultDto;
import co.jinear.core.model.dto.media.WaitingMediaResultDto;
import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.material.MaterialFileInitializeVo;
import co.jinear.core.model.vo.material.MaterialFolderInitializeVo;
import co.jinear.core.model.vo.material.MaterialInitializeVo;
import co.jinear.core.model.vo.media.InitializeWaitingMediaVo;
import co.jinear.core.repository.material.MaterialRepository;
import co.jinear.core.service.media.MediaOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialOperationService {

    private final MediaOperationService mediaOperationService;
    private final MaterialRepository materialRepository;
    private final MaterialEntityConverter materialEntityConverter;
    private final MaterialRetrieveService materialRetrieveService;

    public void initialize(MaterialFolderInitializeVo materialFolderInitializeVo) {
        log.info("Initialize folder has started. materialFolderInitializeVo: {}", materialFolderInitializeVo);
        validateParentMaterialIsFolderAndWithinSameWorkspace(materialFolderInitializeVo);
        Material material = materialEntityConverter.convert(materialFolderInitializeVo);
        materialRepository.save(material);
    }

    public WaitingForUploadMaterialResultDto initialize(MaterialFileInitializeVo materialFileInitializeVo) {
        log.info("Initialize and retrieve file upload url has started. materialFileInitializeVo: {}", materialFileInitializeVo);
        validateParentMaterialIsFolderAndWithinSameWorkspace(materialFileInitializeVo);
        Material material = materialEntityConverter.convert(materialFileInitializeVo);
        Material saved = materialRepository.save(material);
        InitializeWaitingMediaVo initializeWaitingMediaVo = mapInitializeWaitingMediaVo(saved, materialFileInitializeVo);
        WaitingMediaResultDto waitingMediaResultDto = mediaOperationService.initializeWaitingMediaAndGetPresignedUploadUrl(initializeWaitingMediaVo);
        updateMediaId(saved, waitingMediaResultDto, material);
        return new WaitingForUploadMaterialResultDto(saved.getMaterialId(), waitingMediaResultDto);
    }

    public void notifyUploadComplete(String materialId) {
        log.info("Notify upload complete has started. materialId: {}", materialId);
        Material material = materialRetrieveService.retrieveEntity(materialId);
        mediaOperationService.updateAsUploadCompleted(material.getMediaId());
    }

    private InitializeWaitingMediaVo mapInitializeWaitingMediaVo(Material saved, MaterialFileInitializeVo materialFileInitializeVo) {
        InitializeWaitingMediaVo initializeWaitingMediaVo = new InitializeWaitingMediaVo();
        initializeWaitingMediaVo.setOwnerId(saved.getOwnerId());
        initializeWaitingMediaVo.setRelatedObjectId(saved.getMaterialId());
        initializeWaitingMediaVo.setFileType(FileType.MATERIAL_MEDIA);
        initializeWaitingMediaVo.setMediaOwnerType(MediaOwnerType.MATERIAL);
        initializeWaitingMediaVo.setVisibility(MediaVisibilityType.PRIVATE);
        initializeWaitingMediaVo.setOriginalName(saved.getName());
        initializeWaitingMediaVo.setContentType(materialFileInitializeVo.getContentType());
        initializeWaitingMediaVo.setFileSize(materialFileInitializeVo.getFileSize());
        return initializeWaitingMediaVo;
    }

    private void updateMediaId(Material saved, WaitingMediaResultDto waitingMediaResultDto, Material material) {
        saved.setMediaId(waitingMediaResultDto.getMediaId());
        materialRepository.save(material);
    }

    private void validateParentMaterialIsFolderAndWithinSameWorkspace(MaterialInitializeVo materialInitializeVo) {
        String parentMaterialId = materialInitializeVo.getParentMaterialId();
        String workspaceId = materialInitializeVo.getWorkspaceId();
        if (Objects.nonNull(parentMaterialId)) {
            log.info("Validate parent material is folder and within same workspace has started. parentMaterialId: {}, workspaceId: {}", parentMaterialId, workspaceId);
            boolean isFolderAndSameWorkspace = materialRepository.existsByMaterialIdAndMaterialTypeAndWorkspaceIdAndPassiveIdIsNull(parentMaterialId, MaterialType.FOLDER, workspaceId);
            if (!isFolderAndSameWorkspace) {
                throw new NotValidException();
            }
        }
    }
}
