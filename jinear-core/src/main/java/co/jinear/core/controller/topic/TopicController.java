package co.jinear.core.controller.topic;

import co.jinear.core.manager.topic.TopicManager;
import co.jinear.core.model.request.topic.TopicInitializeRequest;
import co.jinear.core.model.request.topic.TopicUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.topic.TopicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/topic")
public class TopicController {

    private final TopicManager topicManager;

    @GetMapping("/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public TopicResponse retrieveTopic(@PathVariable String topicId) {
        return topicManager.retrieveTopic(topicId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TopicResponse initializeTopic(@Valid @RequestBody TopicInitializeRequest topicInitializeRequest) {
        return topicManager.initializeTopic(topicInitializeRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TopicResponse updateTopic(@Valid @RequestBody TopicUpdateRequest topicUpdateRequest) {
        return topicManager.updateTopic(topicUpdateRequest);
    }

    @DeleteMapping("/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTopic(@PathVariable String topicId) {
        return topicManager.deleteTopic(topicId);
    }
}
