package co.jinear.core.service.topic;

import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.entity.topic.Topic;
import co.jinear.core.model.vo.topic.TopicDeleteVo;
import co.jinear.core.model.vo.topic.TopicUpdateVo;
import co.jinear.core.repository.TopicRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicUpdateService {

    private final TopicRetrieveService topicRetrieveService;
    private final PassiveService passiveService;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    public TopicDto updateTopic(TopicUpdateVo topicUpdateVo) {
        log.info("Update topic has started. topicUpdateVo: {}", topicUpdateVo);
        Topic topic = topicRetrieveService.retrieveEntity(topicUpdateVo.getTopicId());
        validateNewTagNotExists(topic, topicUpdateVo);
        updateValues(topic, topicUpdateVo);
        Topic saved = topicRepository.save(topic);
        log.info("Update topic has ended. topicId: {}", saved.getTopicId());
        return modelMapper.map(saved, TopicDto.class);
    }

    public void deleteTopic(TopicDeleteVo topicDeleteVo) {
        log.info("Delete topic has started. topicDeleteVo: {}", topicDeleteVo);
        Topic topic = topicRetrieveService.retrieveEntity(topicDeleteVo.getTopicId());
        String passiveId = passiveService.createUserActionPassive(topicDeleteVo.getResponsibleAccountId());
        topic.setPassiveId(passiveId);
        topicRepository.save(topic);
    }

    private void validateNewTagNotExists(Topic topic, TopicUpdateVo topicUpdateVo) {
        boolean tagChanged = !topic.getTag().equalsIgnoreCase(topicUpdateVo.getTag());
        if (Objects.nonNull(topicUpdateVo.getTag()) && tagChanged) {
            topicRetrieveService.validateTagNotExists(topic.getWorkspaceId(), topicUpdateVo.getTag());
        }
    }

    private void updateValues(Topic topic, TopicUpdateVo topicUpdateVo) {
        Optional.of(topicUpdateVo).map(TopicUpdateVo::getColor).ifPresent(topic::setColor);
        Optional.of(topicUpdateVo).map(TopicUpdateVo::getName).ifPresent(topic::setName);
        Optional.of(topicUpdateVo).map(TopicUpdateVo::getTag).ifPresent(topic::setTag);
    }
}
