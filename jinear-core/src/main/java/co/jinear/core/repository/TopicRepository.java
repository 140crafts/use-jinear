package co.jinear.core.repository;

import co.jinear.core.model.entity.topic.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, String> {

    Long countAllByWorkspaceIdAndTagIgnoreCaseAndPassiveIdIsNull(String workspaceId, String tag);

    Optional<Topic> findByTopicIdAndPassiveIdIsNull(String topicId);

    Optional<Topic> findByTeamIdAndWorkspaceIdAndTagAndPassiveIdIsNull(String teamId, String workspaceId, String tag);

    Page<Topic> findAllByNameContainingAndOwnerIdAndPassiveIdIsNullOrderByCreatedDateAsc(String name, String ownerId, Pageable pageable);

    Page<Topic> findAllByNameContainingAndTeamIdAndPassiveIdIsNullOrderByCreatedDateAsc(String name, String teamId, Pageable pageable);

    Page<Topic> findAllByTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(String teamId, Pageable pageable);

    List<Topic> findAllByTeamIdAndTopicIdIsInAndPassiveIdIsNullOrderByCreatedDateDesc(String teamId, List<String> topicId);
}
