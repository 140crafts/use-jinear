package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, String> {

    Optional<ChannelMember> findByChannelMemberIdAndPassiveIdIsNull(String channelMemberId);
}
