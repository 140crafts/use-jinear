package co.jinear.core.service.chat.channel;

import co.jinear.core.converter.chat.channel.ChannelSettingsEntityConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.chat.ChannelSettings;
import co.jinear.core.model.vo.chat.channel.InitializeChannelSettingsVo;
import co.jinear.core.repository.chat.ChannelSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelSettingsService {

    private final ChannelSettingsRepository channelSettingsRepository;
    private final ChannelSettingsEntityConverter channelSettingsEntityConverter;

    public void initialize(String channelId, InitializeChannelSettingsVo initializeChannelSettingsVo) {
        log.info("Initialize channel settings has started. InitializeChannelSettingsVo: {}", initializeChannelSettingsVo);
        ChannelSettings channelSettings = channelSettingsEntityConverter.convert(channelId, initializeChannelSettingsVo);
        channelSettingsRepository.save(channelSettings);
    }

    public void remove(String passiveId, String channelSettingsId) {
        log.info("Remove channel setting has started. channelSettingsId: {}", channelSettingsId);
        ChannelSettings channelSettings = retrieveEntity(channelSettingsId);
        channelSettings.setPassiveId(passiveId);
    }

    private ChannelSettings retrieveEntity(String channelSettingsId) {
        return channelSettingsRepository.findByChannelSettingsIdAndPassiveIdIsNull(channelSettingsId)
                .orElseThrow(NotFoundException::new);
    }
}
