package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.ChannelSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelSettingsRepository extends JpaRepository<ChannelSettings, String> {

    Optional<ChannelSettings> findByChannelSettingsIdAndPassiveIdIsNull(String channelSettingsId);
}
