package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.dto.messaging.channel.ChannelDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.entity.messaging.Channel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ChannelSettingsDtoConverter.class, ChannelMemberDtoConverter.class})
public interface ChannelDtoConverter {

    ChannelDto convert(Channel channel);

    PlainChannelDto convertPlain(Channel channel);
}
