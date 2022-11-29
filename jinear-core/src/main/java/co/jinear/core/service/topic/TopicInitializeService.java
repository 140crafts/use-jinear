package co.jinear.core.service.topic;

import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.entity.topic.Topic;
import co.jinear.core.model.vo.topic.TopicInitializeVo;
import co.jinear.core.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicInitializeService {

    private final TopicRepository topicRepository;
    private final TopicRetrieveService topicRetrieveService;
    private final ModelMapper modelMapper;

    public TopicDto initializeTopic(TopicInitializeVo topicInitializeVo) {
        log.info("Initialize topic has started. topicInitializeVo: {}", topicInitializeVo);
        topicRetrieveService.validateTagNotExists(topicInitializeVo.getWorkspaceId(), topicInitializeVo.getTag());
        Topic topic = mapInitializeVoToEntity(topicInitializeVo);
        Topic saved = topicRepository.save(topic);
        log.info("Initialize topic has ended. topicId: {}", saved.getTopicId());
        return modelMapper.map(saved, TopicDto.class);
    }

    private Topic mapInitializeVoToEntity(TopicInitializeVo topicInitializeVo) {
        Topic topic = new Topic();
        topic.setOwnerId(topicInitializeVo.getOwnerId());
        topic.setWorkspaceId(topicInitializeVo.getWorkspaceId());
        topic.setColor(topicInitializeVo.getColor());
        topic.setName(topicInitializeVo.getName());
        topic.setTag(topicInitializeVo.getTag());
        return topic;
    }
}
