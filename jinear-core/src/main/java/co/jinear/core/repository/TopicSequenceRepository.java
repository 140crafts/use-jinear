package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TopicSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicSequenceRepository extends JpaRepository<TopicSequence, String> {

    Optional<TopicSequence> findByTopicId(String topicId);
}
