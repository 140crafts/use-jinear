package co.jinear.core.model.dto.messaging.channel;

import co.jinear.core.model.enumtype.messaging.ChannelSettingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitialChannelSettingDto {

    private ChannelSettingType settingsType;
    private String settingsValue;
}
