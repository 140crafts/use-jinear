package co.jinear.core.controller.messaging.channel;

import co.jinear.core.manager.messaging.ChannelMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ChannelMemberListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/channel/member")
public class ChannelMemberController {

    private final ChannelMemberManager channelMemberManager;

    @GetMapping("/memberships/{workspaceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelMemberListingResponse retrieveMemberships(@PathVariable String workspaceId) {
        return channelMemberManager.retrieveMemberships(workspaceId);
    }

    @GetMapping("/list/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelMemberListingResponse retrieveChannelMembers(@PathVariable String channelId) {
        return channelMemberManager.retrieveMemberList(channelId);
    }

    @PostMapping("/join/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse join(@PathVariable String channelId) {
        return channelMemberManager.join(channelId);
    }

    @PostMapping("/leave/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse leave(@PathVariable String channelId) {
        return channelMemberManager.leave(channelId);
    }

    @PostMapping("/add/{channelId}/account/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse add(@PathVariable String channelId,
                            @PathVariable String accountId) {
        return channelMemberManager.add(channelId, accountId);
    }

    @PostMapping("/remove/{channelId}/account/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse remove(@PathVariable String channelId,
                               @PathVariable String accountId) {
        return channelMemberManager.remove(channelId, accountId);
    }
}
