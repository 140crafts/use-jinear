package co.jinear.core.repository.feed;

import co.jinear.core.model.entity.feed.FeedMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedMemberRepository extends JpaRepository<FeedMember, String> {

    List<FeedMember> findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);

    Optional<FeedMember> findFirstByAccountIdAndFeedIdAndPassiveIdIsNull(String accountId, String feedId);

    Page<FeedMember> findAllByFeedIdAndPassiveIdIsNull(String feedId, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update FeedMember  feedMember
                set feedMember.passiveId = :passiveId
                    where 
                        feedMember.feedId = :feedId and 
                        feedMember.passiveId is null
                """)
    void removeAllMembers(@Param("feedId") String feedId, @Param("passiveId") String passiveId);
}
