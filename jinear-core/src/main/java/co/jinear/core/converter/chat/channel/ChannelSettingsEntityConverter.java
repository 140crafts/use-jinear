package co.jinear.core.converter.chat.channel;

import co.jinear.core.model.entity.chat.ChannelSettings;
import co.jinear.core.model.vo.chat.channel.InitializeChannelSettingsVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelSettingsEntityConverter {

    ChannelSettings convert(String channelId, InitializeChannelSettingsVo initializeChannelSettingsVo);
}
