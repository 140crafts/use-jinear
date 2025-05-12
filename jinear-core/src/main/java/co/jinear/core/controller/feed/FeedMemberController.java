package co.jinear.core.controller.feed;

import co.jinear.core.manager.feed.FeedMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.feed.FeedMemberListingResponse;
import co.jinear.core.model.response.feed.FeedMemberPaginatedResponse;
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

    @GetMapping("/list/{feedId}")
    @ResponseStatus(HttpStatus.OK)
    public FeedMemberPaginatedResponse retrieveFeedMembers(@PathVariable String feedId,
                                                           @RequestParam(required = false, defaultValue = "0") int page) {
        return feedMemberManager.retrieveFeedMembers(feedId, page);
    }

    @PostMapping("/{feedId}/manage/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse addFeedMember(@PathVariable String feedId,
                                      @PathVariable String accountId) {
        return feedMemberManager.addFeedMember(feedId, accountId);
    }

    @DeleteMapping("/{feedId}/manage/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse kickFeedMember(@PathVariable String feedId,
                                       @PathVariable String accountId) {
        return feedMemberManager.kickFeedMember(feedId, accountId);
    }
}
