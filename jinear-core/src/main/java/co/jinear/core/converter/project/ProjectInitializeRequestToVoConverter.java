package co.jinear.core.converter.project;

import co.jinear.core.model.request.project.ProjectInitializeRequest;
import co.jinear.core.model.vo.project.ProjectInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectInitializeRequestToVoConverter {

    ProjectInitializeVo map(ProjectInitializeRequest projectInitializeRequest);
}
