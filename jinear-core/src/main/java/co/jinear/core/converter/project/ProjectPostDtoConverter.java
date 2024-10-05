package co.jinear.core.converter.project;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.entity.project.ProjectPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class, AccessibleMediaDtoConverter.class})
public interface ProjectPostDtoConverter {

    ProjectPostDto convert(ProjectPost projectPost);
}
