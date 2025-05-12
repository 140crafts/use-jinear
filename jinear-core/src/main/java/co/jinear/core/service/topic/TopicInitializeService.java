package co.jinear.core.service.topic;

import co.jinear.core.converter.topic.TopicDtoConverter;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.entity.topic.Topic;
import co.jinear.core.model.vo.topic.TopicInitializeVo;
import co.jinear.core.repository.TopicRepository;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicInitializeService {

    private final TopicRepository topicRepository;
    private final TopicRetrieveService topicRetrieveService;
    private final TopicDtoConverter topicDtoConverter;

    public TopicDto initializeTopic(TopicInitializeVo topicInitializeVo) {
        log.info("Initialize topic has started. topicInitializeVo: {}", topicInitializeVo);
        topicRetrieveService.validateTagNotExists(topicInitializeVo.getWorkspaceId(), topicInitializeVo.getTag().toUpperCase(Locale.ROOT));
        Topic topic = mapInitializeVoToEntity(topicInitializeVo);
        Topic saved = topicRepository.save(topic);
        log.info("Initialize topic has ended. topicId: {}", saved.getTopicId());
        return topicDtoConverter.map(saved);
    }

    private Topic mapInitializeVoToEntity(TopicInitializeVo topicInitializeVo) {
        Topic topic = new Topic();
        topic.setOwnerId(topicInitializeVo.getOwnerId());
        topic.setWorkspaceId(topicInitializeVo.getWorkspaceId());
        topic.setTeamId(topicInitializeVo.getTeamId());
        topic.setColor(topicInitializeVo.getColor());
        topic.setName(topicInitializeVo.getName());
        topic.setTag(NormalizeHelper.normalizeStrictly(topicInitializeVo.getTag()));
        return topic;
    }
}
