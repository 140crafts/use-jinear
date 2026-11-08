package co.jinear.core.service.material;

import co.jinear.core.converter.material.MaterialDtoConverter;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.entity.material.MaterialFts;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.vo.material.MaterialFtsSearchVo;
import co.jinear.core.model.vo.material.MaterialSearchVo;
import co.jinear.core.repository.material.MaterialFtsSearchRepository;
import co.jinear.core.repository.material.MaterialSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialSearchService {

    private static final int PAGE_SIZE = 50;

    private final MaterialSearchRepository materialSearchRepository;
    private final MaterialDtoConverter materialDtoConverter;
    private final MaterialFtsSearchRepository materialFtsSearchRepository;
    private final MaterialRetrieveService materialRetrieveService;

    public Page<MaterialDto> search(MaterialSearchVo materialSearchVo) {
        log.info("Search material has started. materialSearchVo: {}", materialSearchVo);
        return materialSearchRepository.search(materialSearchVo)
                .map(materialDtoConverter::convert);
    }

    public Page<MaterialDto> searchText(MaterialFtsSearchVo materialFtsSearchVo) {
        log.info("Search material text has started. materialFtsSearchVo: {}", materialFtsSearchVo);
        Pageable pageable = PageRequest.of(materialFtsSearchVo.getPage(), PAGE_SIZE);
        if (StringUtils.isBlank(materialFtsSearchVo.getQuery())) {
            return Page.empty(pageable);
        }
        Integer materialType = Optional.ofNullable(materialFtsSearchVo.getMaterialType())
                .map(MaterialType::getValue)
                .orElse(null);
        Page<MaterialFts> ranked = materialFtsSearchRepository.search(
                materialFtsSearchVo.getQuery(),
                materialFtsSearchVo.getWorkspaceId(),
                materialType,
                materialFtsSearchVo.getAccountId(),
                materialFtsSearchVo.isWorkspaceAdmin(),
                pageable);
        List<String> materialIds = ranked.map(MaterialFts::getMaterialId).getContent();
        List<MaterialDto> materials = retrieveInOrder(materialIds);
        return new PageImpl<>(materials, pageable, ranked.getTotalElements());
    }

    private List<MaterialDto> retrieveInOrder(List<String> materialIds) {
        List<MaterialDto> materials = materialRetrieveService.retrieveAll(materialIds);
        Map<String, MaterialDto> materialsById = materials.stream()
                .collect(Collectors.toMap(MaterialDto::getMaterialId, Function.identity()));
        return materialIds.stream()
                .map(materialsById::get)
                .filter(Objects::nonNull)
                .toList();
    }

}
