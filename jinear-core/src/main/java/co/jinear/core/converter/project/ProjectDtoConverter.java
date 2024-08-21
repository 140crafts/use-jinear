package co.jinear.core.converter.project;


import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.converter.workspace.WorkspaceMemberDtoConverter;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.entity.project.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WorkspaceMemberDtoConverter.class, RichTextConverter.class, ProjectTeamDtoConverter.class, MilestoneDtoConverter.class})
public interface ProjectDtoConverter {

    ProjectDto convert(Project project);
}
