package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.request.messaging.channel.InitializeChannelRequest;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeChannelRequestConverter {

    InitializeChannelVo convert(InitializeChannelRequest initializeChannelRequest, String initializedBy);
}
