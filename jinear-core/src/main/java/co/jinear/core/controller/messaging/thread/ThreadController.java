package co.jinear.core.controller.messaging.thread;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/thread")
public class ThreadController {

//    @PostMapping("/workspace/{workspaceId}/{feedId}")
//    @ResponseStatus(HttpStatus.OK)
//    public FeedContentResponse retrieveFeedContent(@PathVariable String workspaceId,
//                                                   @PathVariable String feedId,
//                                                   @RequestParam(required = false) String pageToken) {
//        return feedManager.retrieveFeed(workspaceId, feedId, pageToken);
//    }

}
