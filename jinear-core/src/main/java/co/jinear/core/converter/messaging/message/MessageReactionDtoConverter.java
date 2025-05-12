package co.jinear.core.converter.messaging.message;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.messaging.message.MessageReactionDto;
import co.jinear.core.model.entity.messaging.MessageReaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = PlainAccountProfileDtoConverter.class)
public interface MessageReactionDtoConverter {

    MessageReactionDto convert(MessageReaction messageReaction);
}
