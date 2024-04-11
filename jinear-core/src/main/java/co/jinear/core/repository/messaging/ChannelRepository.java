package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Channel;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findByChannelIdAndPassiveIdIsNull(String channelId);

    List<Channel> findAllByWorkspaceIdAndPassiveIdIsNull(String workspaceId);

    List<Channel> findAllByWorkspaceIdAndChannelVisibilityTypeIsInAndPassiveIdIsNull(String workspaceId, List<ChannelVisibilityType> channelVisibilityTypeList);
}
