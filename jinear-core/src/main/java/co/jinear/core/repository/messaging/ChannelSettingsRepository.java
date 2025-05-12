package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.ChannelSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelSettingsRepository extends JpaRepository<ChannelSettings, String> {

    Optional<ChannelSettings> findByChannelSettingsIdAndPassiveIdIsNull(String channelSettingsId);
}
