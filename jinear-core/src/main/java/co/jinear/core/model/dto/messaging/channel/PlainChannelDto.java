package co.jinear.core.model.dto.messaging.channel;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlainChannelDto extends BaseDto {

    private String channelId;
    private String workspaceId;
    private String title;
    private ChannelParticipationType participationType;
    private ChannelVisibilityType channelVisibilityType;
}
