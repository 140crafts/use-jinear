package co.jinear.core.controller.messaging.channel;

import co.jinear.core.manager.messaging.ChannelSettingsManager;
import co.jinear.core.model.request.messaging.channel.InitializeChannelSettingsRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/channel/setting")
public class ChannelSettingsController {

    private final ChannelSettingsManager channelSettingsManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initialize(@Valid @RequestBody InitializeChannelSettingsRequest initializeChannelSettingsRequest) {
        return channelSettingsManager.initialize(initializeChannelSettingsRequest);
    }

    @DeleteMapping("/{channelSettingsId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse remove(@PathVariable String channelSettingsId) {
        return channelSettingsManager.remove(channelSettingsId);
    }
}
