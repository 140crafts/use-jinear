package co.jinear.core.converter.messaging.message;


import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.messaging.conversation.ConversationDtoConverter;
import co.jinear.core.converter.messaging.thread.ThreadDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.entity.messaging.Message;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RichTextConverter.class, PlainAccountProfileDtoConverter.class, MessageReactionDtoConverter.class, ThreadDtoConverter.class, ConversationDtoConverter.class})
public interface MessageDtoConverter {

    @Named("convertMessageDto")
    MessageDto convert(Message message);

    @Named("convertRichMessageDto")
    RichMessageDto convertRich(Message message);
}
