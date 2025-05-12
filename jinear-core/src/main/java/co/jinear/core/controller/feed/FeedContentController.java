package co.jinear.core.controller.feed;

import co.jinear.core.manager.feed.FeedManager;
import co.jinear.core.model.response.team.FeedContentItemResponse;
import co.jinear.core.model.response.team.FeedContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/feed/content")
public class FeedContentController {

    private final FeedManager feedManager;

    @GetMapping("/workspace/{workspaceId}/{feedId}")
    @ResponseStatus(HttpStatus.OK)
    public FeedContentResponse retrieveFeedContent(@PathVariable String workspaceId,
                                                   @PathVariable String feedId,
                                                   @RequestParam(required = false) String pageToken) {
        return feedManager.retrieveFeed(workspaceId, feedId, pageToken);
    }

    @GetMapping("/workspace/{workspaceId}/{feedId}/item/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public FeedContentItemResponse retrieveFeedContentItem(@PathVariable String workspaceId,
                                                           @PathVariable String feedId,
                                                           @PathVariable String itemId) {
        return feedManager.retrieveFeedItem(workspaceId, feedId, itemId);
    }
}
