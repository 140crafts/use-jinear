package co.jinear.core.converter.project;

import co.jinear.core.model.request.project.ProjectFeedSettingsOperationRequest;
import co.jinear.core.model.vo.project.ProjectFeedSettingsOperationVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectFeedSettingsOperationRequestToVoConverter {

    ProjectFeedSettingsOperationVo convert(ProjectFeedSettingsOperationRequest projectFeedSettingsOperationRequest);
}
