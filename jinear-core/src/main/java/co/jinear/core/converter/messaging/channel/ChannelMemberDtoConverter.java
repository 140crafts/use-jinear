package co.jinear.core.converter.messaging.channel;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.model.entity.messaging.ChannelMember;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = PlainAccountProfileDtoConverter.class)
public interface ChannelMemberDtoConverter {

    ChannelMemberDto convert(ChannelMember channelMember);
}
