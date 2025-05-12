package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.dto.messaging.channel.ChannelDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.entity.messaging.Channel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ChannelSettingsDtoConverter.class, ChannelMemberDtoConverter.class})
public interface ChannelDtoConverter {
    @Named("convertChannelDto")
    ChannelDto convert(Channel channel);

    @Named("convertPlainChannelDto")
    PlainChannelDto convertPlain(Channel channel);
}
