package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.entity.messaging.ChannelSettings;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelSettingsVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelSettingsEntityConverter {

    ChannelSettings convert(String channelId, InitializeChannelSettingsVo initializeChannelSettingsVo);
}
