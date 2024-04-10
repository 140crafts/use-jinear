package co.jinear.core.converter.messaging.thread;

import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ThreadMessageInfoDtoConverter.class})
public interface ThreadDtoConverter {

    ThreadDto convert(Thread thread);
}
