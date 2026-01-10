package co.jinear.core.converter.material;

import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.model.vo.material.MaterialSearchVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialSearchRequestToVoConverter {

    MaterialSearchVo convert(MaterialSearchRequest materialSearchRequest, String accountIdPerspective);
}
