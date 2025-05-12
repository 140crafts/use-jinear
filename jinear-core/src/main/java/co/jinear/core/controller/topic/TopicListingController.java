package co.jinear.core.controller.topic;

import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.model.request.topic.RetrieveTopicListRequest;
import co.jinear.core.model.response.topic.TopicListingResponse;
import co.jinear.core.model.response.topic.TopicSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/topic/list")
public class TopicListingController {

    private final TopicListingManager topicListingManager;

    @GetMapping("/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TopicListingResponse retrieveTeamTopics(@PathVariable String teamId, @RequestParam(required = false, defaultValue = "0") Integer page) {
        return topicListingManager.retrieveTeamTopics(teamId, page);
    }

    @PostMapping("/{teamId}/retrieve-exact")
    @ResponseStatus(HttpStatus.OK)
    public TopicSearchResponse retrieveExactTeamTopics(@PathVariable String teamId, @RequestBody RetrieveTopicListRequest retrieveTopicListRequest) {
        return topicListingManager.retrieveTeamExactTopics(teamId, retrieveTopicListRequest);
    }

    @GetMapping("/{teamId}/search")
    @ResponseStatus(HttpStatus.OK)
    public TopicSearchResponse searchTeamTopics(@PathVariable String teamId, @RequestParam(required = false, defaultValue = "") String nameOrTag) {
        return topicListingManager.searchTeamTopics(teamId, nameOrTag);
    }
}
