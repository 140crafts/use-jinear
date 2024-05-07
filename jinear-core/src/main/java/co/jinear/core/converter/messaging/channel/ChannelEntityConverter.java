package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.entity.messaging.Channel;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelEntityConverter {

    Channel convert(InitializeChannelVo initializeChannelVo);
}
