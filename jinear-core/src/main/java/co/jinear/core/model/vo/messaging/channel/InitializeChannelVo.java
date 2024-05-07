package co.jinear.core.model.vo.messaging.channel;

import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class InitializeChannelVo {

    private String initializedBy;
    private String workspaceId;
    private String title;
    private ChannelParticipationType participationType;
    private ChannelVisibilityType channelVisibilityType;
    @ToString.Exclude
    private List<InitializeChannelSettingsVo> initialSettings;
}
