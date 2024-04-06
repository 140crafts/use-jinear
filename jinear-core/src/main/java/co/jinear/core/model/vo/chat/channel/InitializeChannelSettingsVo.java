package co.jinear.core.model.vo.chat.channel;

import co.jinear.core.model.enumtype.chat.ChannelSettingType;
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
