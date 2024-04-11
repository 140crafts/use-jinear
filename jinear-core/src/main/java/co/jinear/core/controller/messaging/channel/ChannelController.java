package co.jinear.core.controller.messaging.channel;

import co.jinear.core.manager.messaging.ChannelManager;
import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.model.request.messaging.channel.InitializeChannelRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ChannelListingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/channel")
public class ChannelController {

    private final ChannelManager channelManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeChannel(@Valid @RequestBody InitializeChannelRequest initializeChannelRequest) {
        return channelManager.initializeChannel(initializeChannelRequest);
    }

    @GetMapping("/workspace/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public ChannelListingResponse listChannels(@PathVariable String workspaceId) {
        return channelManager.listChannels(workspaceId);
    }

    @PutMapping("/{channelId}/participation/{participationType}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateParticipation(@PathVariable String channelId,
                                            @PathVariable ChannelParticipationType participationType) {
        return channelManager.updateParticipation(channelId, participationType);
    }

    @PutMapping("/{channelId}/visibility/{channelVisibilityType}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateVisibility(@PathVariable String channelId,
                                            @PathVariable ChannelVisibilityType channelVisibilityType) {
        return channelManager.updateVisibility(channelId, channelVisibilityType);
    }
}
