package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationMessageInfoDto;
import co.jinear.core.model.entity.messaging.ConversationMessageInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {MessageDtoConverter.class, PlainAccountProfileDtoConverter.class})
public interface ConversationMessageInfoDtoConverter {

    @BeanMapping(qualifiedByName = "convertMessageDto")
    ConversationMessageInfoDto convert(ConversationMessageInfo conversationMessageInfo);
}
