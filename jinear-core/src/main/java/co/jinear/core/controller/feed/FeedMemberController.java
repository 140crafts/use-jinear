package co.jinear.core.controller.feed;

import co.jinear.core.manager.feed.FeedMemberManager;
import co.jinear.core.model.response.feed.FeedMemberListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/feed/member")
public class FeedMemberController {

    private final FeedMemberManager feedMemberManager;

    @GetMapping("/memberships/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public FeedMemberListingResponse retrieveMemberships(@PathVariable String workspaceId) {
        return feedMemberManager.retrieveMemberFeeds(workspaceId);
    }
}
