package co.jinear.core.repository.feed;

import co.jinear.core.model.entity.feed.FeedMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedMemberRepository extends JpaRepository<FeedMember, String> {

    List<FeedMember> findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);
}
