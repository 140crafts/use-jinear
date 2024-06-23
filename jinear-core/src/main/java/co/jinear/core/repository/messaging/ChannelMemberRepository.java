package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.ChannelMember;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, String> {

    Optional<ChannelMember> findByChannelMemberIdAndPassiveIdIsNull(String channelMemberId);

    Optional<ChannelMember> findByChannelIdAndAccountIdAndPassiveIdIsNull(String channelId, String accountId);

    Long countAllByChannelIdAndAccountIdAndRoleTypeIsInAndPassiveIdIsNull(String channelId, String accountId, List<ChannelMemberRoleType> roleTypes);

    Long countAllByChannelIdAndRobotIdAndRoleTypeIsInAndPassiveIdIsNull(String channelId, String robotId, List<ChannelMemberRoleType> roleTypes);

    Long countAllByChannelIdAndRoleTypeIsInAndPassiveIdIsNullAndAccount_PassiveIdIsNullAndAccount_Ghost(String channelId, List<ChannelMemberRoleType> roleTypes, Boolean ghost);

    boolean existsByChannelIdAndAccountIdAndPassiveIdIsNull(String channelId, String accountId);

    boolean existsByChannelIdAndRobotIdAndPassiveIdIsNull(String channelId, String robotId);

    List<ChannelMember> findAllByChannel_WorkspaceIdAndAccountIdAndPassiveIdIsNull(String workspaceId, String accountId);

    List<ChannelMember> findAllByChannelIdAndPassiveIdIsNull(String channelId);

    List<ChannelMember> findAllByChannelIdAndAccountIdIsNotNullAndPassiveIdIsNull(String channelId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ChannelMember cm
                set
                 cm.lastCheck = :lastCheck,
                 cm.lastUpdatedDate = current_timestamp()
                where
                     cm.channelId = :channelId and
                     cm.accountId = :accountId and
                     cm.passiveId is null
                """)
    void updateLastCheck(@Param("channelId") String channelId,
                         @Param("accountId") String accountId,
                         @Param("lastCheck") ZonedDateTime lastCheck);
}
