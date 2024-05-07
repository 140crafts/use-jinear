package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationDto;
import co.jinear.core.model.entity.messaging.Conversation;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ConversationMessageInfoDtoConverter.class, ConversationParticipantDtoConverter.class, PlainAccountProfileDtoConverter.class})
public interface ConversationDtoConverter {

    @BeanMapping(qualifiedByName = "convertPlainParticipantDto")
    ConversationDto convert(Conversation conversation);
}
