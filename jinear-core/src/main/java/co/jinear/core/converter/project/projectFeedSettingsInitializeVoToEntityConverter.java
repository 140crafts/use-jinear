package co.jinear.core.converter.project;

import co.jinear.core.model.entity.project.ProjectFeedSettings;
import co.jinear.core.model.vo.project.ProjectFeedSettingsInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface projectFeedSettingsInitializeVoToEntityConverter {

    ProjectFeedSettings convert(ProjectFeedSettingsInitializeVo projectFeedSettingsInitializeVo, String accessKey);
}
