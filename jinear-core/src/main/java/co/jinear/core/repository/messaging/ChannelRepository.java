package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findByChannelIdAndPassiveIdIsNull(String channelId);
}
