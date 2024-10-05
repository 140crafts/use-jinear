package co.jinear.core.converter.project;

import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.converter.workspace.WorkspaceDtoConverter;
import co.jinear.core.converter.workspace.WorkspaceMemberDtoConverter;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.project.PublicProjectDto;
import co.jinear.core.model.entity.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {WorkspaceDtoConverter.class,
                WorkspaceMemberDtoConverter.class,
                RichTextConverter.class,
                ProjectTeamDtoConverter.class,
                MilestoneDtoConverter.class,
                ProjectFeedSettingsDtoConverter.class})
public interface ProjectDtoConverter {

    ProjectDto convert(Project project);

    @Mapping(source = "projectFeedSettings.info", target = "info")
    PublicProjectDto convertToPublic(Project project);
}
