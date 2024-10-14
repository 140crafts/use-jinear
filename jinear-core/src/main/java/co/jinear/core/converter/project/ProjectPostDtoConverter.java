package co.jinear.core.converter.project;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.entity.project.ProjectPost;
import co.jinear.core.service.project.ProjectPostCommentListingService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class, AccessibleMediaDtoConverter.class})
public abstract class ProjectPostDtoConverter {

    @Autowired
    private ProjectPostCommentListingService projectPostCommentListingService;

    public abstract ProjectPostDto convert(ProjectPost projectPost);

    @AfterMapping
    void afterMap(@MappingTarget ProjectPostDto projectPostDto) {
        Long commentCount = projectPostCommentListingService.commentCount(projectPostDto.getProjectPostId());
        projectPostDto.setCommentCount(commentCount);
    }
}
