package co.jinear.core.converter.material;

import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.entity.material.Material;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccessibleMediaDtoConverter.class})
public interface MaterialDtoConverter {

    MaterialDto convert(Material material);
}
