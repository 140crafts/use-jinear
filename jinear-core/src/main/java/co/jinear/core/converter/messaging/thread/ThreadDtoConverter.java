package co.jinear.core.converter.messaging.thread;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.messaging.thread.PlainThreadDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.entity.messaging.Thread;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ThreadMessageInfoDtoConverter.class, PlainAccountProfileDtoConverter.class})
public interface ThreadDtoConverter {

    ThreadDto convert(Thread thread);

    PlainThreadDto convertPlain(Thread thread);
}
