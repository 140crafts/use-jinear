package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, String> {
}
