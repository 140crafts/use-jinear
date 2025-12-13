package co.jinear.core.manager.material;

import co.jinear.core.converter.material.MaterialSearchRequestToVoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.material.MaterialHierarchyDto;
import co.jinear.core.model.dto.material.PathAwareMaterialDto;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.model.response.material.ParentMaterialDtoResponse;
import co.jinear.core.model.vo.material.MaterialSearchVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.material.MaterialSearchService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialListingManager {

    private final SessionInfoService sessionInfoService;
    private final MaterialAccessValidationService materialAccessValidationService;
    private final MaterialSearchService materialSearchService;
    private final WorkspaceValidator workspaceValidator;
    private final MaterialSearchRequestToVoConverter materialSearchRequestToVoConverter;
    private final MaterialRetrieveService materialRetrieveService;

    public ParentMaterialDtoResponse search(MaterialSearchRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, request.getWorkspaceId());
        validateParentMaterialIsInGivenWorkspace(request);
        log.info("Retrieve folder content has started. currentAccountId: {}, request: {}", currentAccountId, request);
        MaterialSearchVo materialSearchVo = materialSearchRequestToVoConverter.convert(request);
        Page<MaterialDto> searchResult = materialSearchService.search(materialSearchVo);
        Optional<PathAwareMaterialDto> pathAwareMaterialDto = Optional.of(materialSearchVo)
                .map(MaterialSearchVo::getParentMaterialId)
                .map(materialRetrieveService::retrievePathAware);
        return mapResponse(pathAwareMaterialDto, searchResult);
    }

    private void validateParentMaterialIsInGivenWorkspace(MaterialSearchRequest request) {
        if (Objects.nonNull(request.getParentMaterialId())) {
            materialAccessValidationService.validateMaterialIsInWorkspace(request.getParentMaterialId(), request.getWorkspaceId());
        }
    }

    private ParentMaterialDtoResponse mapResponse(Optional<PathAwareMaterialDto> pathAwareMaterialDto, Page<MaterialDto> searchResult) {
        MaterialHierarchyDto materialHierarchyDto = new MaterialHierarchyDto();
        materialHierarchyDto.setContent(new PageDto<>(searchResult));
        pathAwareMaterialDto.ifPresent(materialHierarchyDto::setContainer);

        ParentMaterialDtoResponse paginatedMaterialSearchResponse = new ParentMaterialDtoResponse();
        paginatedMaterialSearchResponse.setMaterialHierarchyDto(materialHierarchyDto);
        return paginatedMaterialSearchResponse;
    }

}
