package co.jinear.core.converter.project;

import co.jinear.core.model.request.project.ProjectDatesUpdateRequest;
import co.jinear.core.model.vo.project.UpdateProjectDatesVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectDatesUpdateRequestToVoConverter {

    UpdateProjectDatesVo convert(ProjectDatesUpdateRequest projectDatesUpdateRequest);
}
