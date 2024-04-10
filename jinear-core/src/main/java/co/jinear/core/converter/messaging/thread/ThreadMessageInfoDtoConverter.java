package co.jinear.core.converter.messaging.thread;

import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.model.dto.messaging.thread.ThreadMessageInfoDto;
import co.jinear.core.model.entity.messaging.ThreadMessageInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = MessageDtoConverter.class)
public interface ThreadMessageInfoDtoConverter {

    ThreadMessageInfoDto convert(ThreadMessageInfo threadMessageInfo);
}
