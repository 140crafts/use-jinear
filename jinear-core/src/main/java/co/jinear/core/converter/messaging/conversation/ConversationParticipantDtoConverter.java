package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.dto.messaging.conversation.PlainConversationParticipantDto;
import co.jinear.core.model.entity.messaging.ConversationParticipant;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {PlainAccountProfileDtoConverter.class, ConversationDtoConverter.class})
public interface ConversationParticipantDtoConverter {

    @Named("convertParticipantDto")
    ConversationParticipantDto convert(ConversationParticipant conversationParticipant);

    @Named("convertPlainParticipantDto")
    PlainConversationParticipantDto convertPlain(ConversationParticipant conversationParticipant);
}
