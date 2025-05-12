package co.jinear.core.controller.messaging.channel;

import co.jinear.core.manager.messaging.ChannelMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ChannelMemberListingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/channel/member")
public class ChannelMemberController {

    private final ChannelMemberManager channelMemberManager;

    @GetMapping("/memberships/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public ChannelMemberListingResponse retrieveMemberships(@PathVariable String workspaceId) {
        return channelMemberManager.retrieveMemberships(workspaceId);
    }

    @GetMapping("/list/{channelId}")
    @ResponseStatus(HttpStatus.OK)
    public ChannelMemberListingResponse retrieveChannelMembers(@PathVariable String channelId) {
        return channelMemberManager.retrieveMemberList(channelId);
    }

    @PostMapping("/join/{channelId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse join(@PathVariable String channelId) {
        return channelMemberManager.join(channelId);
    }


    @PostMapping("/leave/{channelId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse leave(@PathVariable String channelId) {
        return channelMemberManager.leave(channelId);
    }

    @PostMapping("/add/{channelId}/account/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse add(@PathVariable String channelId,
                            @PathVariable String accountId) {
        return channelMemberManager.add(channelId, accountId);
    }

    @PostMapping("/add/{channelId}/robot/{robotId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse addRobot(@PathVariable String channelId,
                                 @PathVariable String robotId) {
        return channelMemberManager.addRobot(channelId, robotId);
    }

    @PostMapping("/remove/{channelId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse remove(@PathVariable String channelId,
                               @PathVariable String accountId) {
        return channelMemberManager.remove(channelId, accountId);
    }

    @PostMapping("/channel/{channelId}/mute")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse mute(@PathVariable String channelId,
                             @Valid @RequestParam("silentUntil") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime silentUntil) {
        return channelMemberManager.mute(channelId, silentUntil);
    }

    @PostMapping("/authorize/{channelId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse authorize(@PathVariable String channelId,
                                  @PathVariable String accountId) {
        return channelMemberManager.authorize(channelId, accountId);
    }

    @PostMapping("/un-authorize/{channelId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse unAuthorize(@PathVariable String channelId,
                                    @PathVariable String accountId) {
        return channelMemberManager.unAuthorize(channelId, accountId);
    }
}
