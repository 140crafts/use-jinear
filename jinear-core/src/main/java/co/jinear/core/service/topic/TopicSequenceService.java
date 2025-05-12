package co.jinear.core.service.topic;

import co.jinear.core.model.entity.task.TopicSequence;
import co.jinear.core.repository.TopicSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicSequenceService {

    private final TopicSequenceRepository topicSequenceRepository;

    @Transactional
    public int incrementTopicSequence(String topicId) {
        log.info("Increment topic sequence has started. topicId: {}", topicId);
        TopicSequence topicSequence = topicSequenceRepository.findByTopicId(topicId)
                .orElseGet(() -> getInitialEntity(topicId));
        int nextSeq = topicSequence.getSeq() + 1;
        topicSequence.setSeq(nextSeq);
        topicSequenceRepository.saveAndFlush(topicSequence);
        return nextSeq;
    }

    private TopicSequence getInitialEntity(String topicId) {
        TopicSequence topicSequence = new TopicSequence();
        topicSequence.setTopicId(topicId);
        topicSequence.setSeq(0);
        return topicSequence;
    }
}
