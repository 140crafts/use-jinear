package co.jinear.core.converter.project;

import co.jinear.core.model.request.project.ProjectPostInitializeRequest;
import co.jinear.core.model.vo.project.InitializeProjectPostVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeProjectPostVoConverter {

    InitializeProjectPostVo convert(ProjectPostInitializeRequest projectPostInitializeRequest, String accountId);
}
