package co.jinear.core.model.request.messaging.channel;

import co.jinear.core.model.dto.messaging.channel.InitialChannelSettingDto;
import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
public class InitializeChannelRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @NotBlank
    private String title;
    @NotNull
    private ChannelParticipationType participationType;
    @NotNull
    private ChannelVisibilityType channelVisibilityType;
    @Nullable
    private List<InitialChannelSettingDto> initialSettings;
}
