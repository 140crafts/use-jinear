package co.jinear.core.service.material;

import co.jinear.core.converter.material.MaterialDtoConverter;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.vo.material.MaterialSearchVo;
import co.jinear.core.repository.material.MaterialSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialSearchService {

    private final MaterialSearchRepository materialSearchRepository;
    private final MaterialDtoConverter materialDtoConverter;

    public Page<MaterialDto> search(MaterialSearchVo materialSearchVo) {
        log.info("Search material has started. materialSearchVo: {}", materialSearchVo);
        return materialSearchRepository.search(materialSearchVo)
                .map(materialDtoConverter::convert);
    }

}
