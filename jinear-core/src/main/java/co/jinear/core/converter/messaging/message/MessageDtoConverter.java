package co.jinear.core.converter.messaging.message;


import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.entity.messaging.Message;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RichTextConverter.class, PlainAccountProfileDtoConverter.class, MessageReactionDtoConverter.class})
public interface MessageDtoConverter {

    MessageDto convert(Message message);
}
