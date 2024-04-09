package co.jinear.core.model.dto.messaging.channel;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.messaging.ChannelSettingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelSettingsDto extends BaseDto {

    private String channelSettingsId;
    private String channelId;
    private ChannelSettingType settingsType;
    private String settingsValue;
}
