package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.dto.messaging.channel.ChannelSettingsDto;
import co.jinear.core.model.entity.messaging.ChannelSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelSettingsDtoConverter {

    ChannelSettingsDto convert(ChannelSettings channelSettings);
}
