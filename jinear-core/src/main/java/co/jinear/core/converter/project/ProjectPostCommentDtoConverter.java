package co.jinear.core.converter.project;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.project.ProjectPostCommentDto;
import co.jinear.core.model.entity.project.ProjectPostComment;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Objects;
import java.util.Optional;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                RichTextConverter.class,
                PlainAccountProfileDtoConverter.class
        })
public interface ProjectPostCommentDtoConverter {

    ProjectPostCommentDto convert(ProjectPostComment projectPostComment);

    @AfterMapping
    default void afterMap(@MappingTarget ProjectPostCommentDto projectPostCommentDto) {
        Optional.of(projectPostCommentDto)
                .map(ProjectPostCommentDto::getQuote)
                .filter(quote -> Objects.nonNull(quote.getPassiveId()))
                .ifPresent(quote -> {
                    quote.setCommentBodyRichTextId(null);
                    quote.setCommentBody(null);
                    quote.setQuote(null);
                    projectPostCommentDto.setQuote(quote);
                });

        if (Objects.nonNull(projectPostCommentDto.getPassiveId())) {
            projectPostCommentDto.setCommentBodyRichTextId(null);
            projectPostCommentDto.setCommentBody(null);
            projectPostCommentDto.setQuote(null);
        }
    }
}
