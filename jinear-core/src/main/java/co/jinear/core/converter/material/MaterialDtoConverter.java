package co.jinear.core.converter.material;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.material.MaterialPathDto;
import co.jinear.core.model.dto.material.PathAwareMaterialDto;
import co.jinear.core.model.entity.material.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccessibleMediaDtoConverter.class, MaterialAccessDtoConverter.class, PlainAccountProfileDtoConverter.class})
public interface MaterialDtoConverter {

    MaterialDto convert(Material material);

    @Mapping(target = "materialId", source = "materialDto.materialId")
    PathAwareMaterialDto convert(MaterialDto materialDto, MaterialPathDto materialPath);
}
