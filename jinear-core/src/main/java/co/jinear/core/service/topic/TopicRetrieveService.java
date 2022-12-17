package co.jinear.core.service.topic;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.entity.topic.Topic;
import co.jinear.core.model.vo.topic.TopicInitializeVo;
import co.jinear.core.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicRetrieveService {

    private final int PAGE_SIZE = 250;

    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    public Topic retrieveEntity(String topicId) {
        return topicRepository.findByTopicIdAndPassiveIdIsNull(topicId)
                .orElseThrow(NotFoundException::new);
    }

    public TopicDto retrieve(String topicId) {
        return topicRepository.findByTopicIdAndPassiveIdIsNull(topicId)
                .map(topic -> modelMapper.map(topic, TopicDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public Page<TopicDto> retrieveTeamTopics(String teamId, int page) {
        log.info("Retrieve team topic page has started. teamId: {}, page: {}", teamId, page);
        return topicRepository.findAllByTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(teamId, PageRequest.of(page, PAGE_SIZE))
                .map(topic -> modelMapper.map(topic, TopicDto.class));
    }

    public void validateTagNotExists(String workspaceId, String tag) {
        boolean exists = topicRepository.countAllByWorkspaceIdAndTagIgnoreCaseAndPassiveIdIsNull(
                workspaceId, tag) > 0L;
        if (exists) {
            throw new BusinessException("topic.init.tag-exists");
        }
    }
}
