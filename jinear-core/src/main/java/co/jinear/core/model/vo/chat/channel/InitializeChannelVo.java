package co.jinear.core.model.vo.chat.channel;

import co.jinear.core.model.enumtype.chat.ChannelParticipationType;
import co.jinear.core.model.enumtype.chat.ChannelVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class InitializeChannelVo {

    private String workspaceId;
    private String title;
    private ChannelParticipationType participationType;
    private ChannelVisibilityType channelVisibilityType;
    @ToString.Exclude
    private List<InitializeChannelSettingsVo> initialSettings;
}
