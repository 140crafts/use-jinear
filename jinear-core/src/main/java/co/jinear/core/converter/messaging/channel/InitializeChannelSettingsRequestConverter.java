package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.request.messaging.channel.InitializeChannelSettingsRequest;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelSettingsVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeChannelSettingsRequestConverter {

    InitializeChannelSettingsVo convert(InitializeChannelSettingsRequest initializeChannelSettingsRequest);
}
