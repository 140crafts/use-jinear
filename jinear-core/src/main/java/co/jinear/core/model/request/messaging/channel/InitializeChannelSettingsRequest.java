package co.jinear.core.model.request.messaging.channel;

import co.jinear.core.model.enumtype.messaging.ChannelSettingType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitializeChannelSettingsRequest extends BaseRequest {

    @NotBlank
    private String channelId;
    @NotNull
    private ChannelSettingType settingsType;
    @NotBlank
    private String settingsValue;
}
