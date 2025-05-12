package co.jinear.core.model.vo.messaging.channel;

import co.jinear.core.model.enumtype.messaging.ChannelSettingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeChannelSettingsVo {

    private ChannelSettingType settingsType;
    private String settingsValue;
}
