package co.jinear.core.converter.task;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.entity.task.Comment;
import co.jinear.core.service.account.AccountRetrieveService;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Objects;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RichTextConverter.class, PlainAccountProfileDtoConverter.class})
public interface CommentDtoConverter {
//can be abstract class so AccountRetrieveService can be autowired

    CommentDto convert(Comment comment, AccountRetrieveService accountRetrieveService);

    @AfterMapping
    default void afterMap(@MappingTarget CommentDto commentDto, AccountRetrieveService accountRetrieveService) {
        PlainAccountProfileDto plainAccountProfileDto = accountRetrieveService.retrievePlainAccountProfile(commentDto.getOwnerId());
        commentDto.setOwner(plainAccountProfileDto);
        if (Objects.nonNull(commentDto.getQuote()) && Objects.nonNull(commentDto.getQuote().getPassiveId())) {
            PlainAccountProfileDto quoteOwner = accountRetrieveService.retrievePlainAccountProfile(commentDto.getQuote().getOwnerId());
            commentDto.getQuote().setOwner(quoteOwner);
        }
    }

    default CommentDto convertAndAnonymize(Comment comment, AccountRetrieveService accountRetrieveService) {
        if (Objects.nonNull(comment)) {
            CommentDto commentDto = convert(comment, accountRetrieveService);
            if (Objects.nonNull(comment.getPassiveId())) {
                commentDto.setRichTextId(null);
                commentDto.setRichText(null);
                commentDto.setQuote(null);
            }
            if (Objects.isNull(comment.getPassiveId()) && Objects.nonNull(comment.getQuote()) && Objects.nonNull(comment.getQuote().getPassiveId())) {
                CommentDto quote = convertAndAnonymize(comment.getQuote(), accountRetrieveService);
                commentDto.setQuote(quote);
            }
            return commentDto;
        }
        return null;
    }
}
