package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.ChannelMember;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, String> {

    Optional<ChannelMember> findByChannelMemberIdAndPassiveIdIsNull(String channelMemberId);

    Optional<ChannelMember> findByChannelIdAndAccountIdAndPassiveIdIsNull(String channelId, String accountId);

    Long countAllByChannelIdAndAccountIdAndRoleTypeIsInAndPassiveIdIsNull(String channelId, String accountId, List<ChannelMemberRoleType> roleTypes);

    boolean existsByChannelIdAndAccountIdAndPassiveIdIsNull(String channelId, String accountId);

    List<ChannelMember> findAllByChannel_WorkspaceIdAndAccountIdAndPassiveIdIsNull(String workspaceId, String accountId);

    List<ChannelMember> findAllByChannelIdAndPassiveIdIsNull(String channelId);
}
