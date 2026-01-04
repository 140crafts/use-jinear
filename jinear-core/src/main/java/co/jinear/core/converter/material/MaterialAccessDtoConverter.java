package co.jinear.core.converter.material;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.material.MaterialAccessDto;
import co.jinear.core.model.entity.material.MaterialAccess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class})
public interface MaterialAccessDtoConverter {

    MaterialAccessDto map(MaterialAccess materialAccess);
}
