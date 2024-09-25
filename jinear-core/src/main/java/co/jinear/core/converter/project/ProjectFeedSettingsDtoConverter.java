package co.jinear.core.converter.project;

import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.project.ProjectFeedSettingsDto;
import co.jinear.core.model.entity.project.ProjectFeedSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RichTextConverter.class})
public interface ProjectFeedSettingsDtoConverter {

    ProjectFeedSettingsDto convert(ProjectFeedSettings projectFeedSettings);
}
