package co.jinear.core.converter.material;

import co.jinear.core.model.request.material.MaterialInitializeFileUploadRequest;
import co.jinear.core.model.vo.material.MaterialFileInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialInitializeFileUploadRequestToVoConverter {

    MaterialFileInitializeVo convert(MaterialInitializeFileUploadRequest materialInitializeFileUploadRequest, String workspaceId, String ownerId);
}
