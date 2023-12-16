package co.jinear.core.controller.feed;

import co.jinear.core.manager.feed.FeedManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/feed")
public class FeedController {

    private final FeedManager feedManager;

    @DeleteMapping("/{feedId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteFeed(@PathVariable String feedId) {
        return feedManager.deleteFeed(feedId);
    }
}
