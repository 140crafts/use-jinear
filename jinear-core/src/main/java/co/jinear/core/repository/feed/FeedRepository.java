package co.jinear.core.repository.feed;

import co.jinear.core.model.entity.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, String> {

    Optional<Feed> findByFeedIdAndPassiveIdIsNull(String feedId);

    Long countAllByWorkspaceIdAndInitializedByAndIntegrationInfoIdAndPassiveIdIsNull(String workspaceId, String accountId, String integrationInfoId);
}
