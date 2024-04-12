package co.jinear.core.converter.messaging.thread;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.model.dto.messaging.thread.ThreadMessageInfoDto;
import co.jinear.core.model.entity.messaging.ThreadMessageInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {MessageDtoConverter.class, PlainAccountProfileDtoConverter.class})
public interface ThreadMessageInfoDtoConverter {

    @BeanMapping(qualifiedByName = "convertMessageDto")
    ThreadMessageInfoDto convert(ThreadMessageInfo threadMessageInfo);
}
