package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.model.dto.messaging.conversation.ConversationDto;
import co.jinear.core.model.entity.messaging.Conversation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ConversationMessageInfoDtoConverter.class})
public interface ConversationDtoConverter {

    ConversationDto convert(Conversation conversation);
}
