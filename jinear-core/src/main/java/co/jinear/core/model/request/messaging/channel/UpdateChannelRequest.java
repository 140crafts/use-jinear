package co.jinear.core.model.request.messaging.channel;

import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class UpdateChannelRequest extends BaseRequest {
    @Nullable
    private String newTitle;
    @Nullable
    private ChannelVisibilityType channelVisibilityType;
    @Nullable
    private ChannelParticipationType participationType;
}
