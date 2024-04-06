package co.jinear.core.converter.chat.channel;

import co.jinear.core.model.entity.chat.Channel;
import co.jinear.core.model.vo.chat.channel.InitializeChannelVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelEntityConverter {

    Channel convert(InitializeChannelVo initializeChannelVo);
}
