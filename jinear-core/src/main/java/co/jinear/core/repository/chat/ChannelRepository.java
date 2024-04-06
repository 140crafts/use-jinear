package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findByChannelIdAndPassiveIdIsNull(String channelId);
}
