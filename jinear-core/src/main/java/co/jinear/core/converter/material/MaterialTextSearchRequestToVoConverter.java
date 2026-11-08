package co.jinear.core.converter.material;

import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.vo.material.MaterialFtsSearchVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialTextSearchRequestToVoConverter {

    @Mapping(target = "query", source = "materialTextSearchRequest.query")
    @Mapping(target = "materialType", source = "materialTextSearchRequest.materialType")
    @Mapping(target = "workspaceId", source = "workspaceId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "workspaceAdmin", source = "workspaceAdmin")
    @Mapping(target = "page", source = "page")
    MaterialFtsSearchVo map(MaterialTextSearchRequest materialTextSearchRequest, String workspaceId, String accountId, boolean workspaceAdmin, int page);
}
