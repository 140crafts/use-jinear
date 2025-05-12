package co.jinear.core.service.messaging.channel;

import co.jinear.core.converter.messaging.channel.ChannelSettingsDtoConverter;
import co.jinear.core.converter.messaging.channel.ChannelSettingsEntityConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.channel.ChannelSettingsDto;
import co.jinear.core.model.entity.messaging.ChannelSettings;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelSettingsVo;
import co.jinear.core.repository.messaging.ChannelSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelSettingsService {

    private final ChannelSettingsRepository channelSettingsRepository;
    private final ChannelSettingsEntityConverter channelSettingsEntityConverter;
    private final ChannelSettingsDtoConverter channelSettingsDtoConverter;

    public void initialize(String channelId, InitializeChannelSettingsVo initializeChannelSettingsVo) {
        log.info("Initialize channel settings has started. InitializeChannelSettingsVo: {}", initializeChannelSettingsVo);
        ChannelSettings channelSettings = channelSettingsEntityConverter.convert(channelId, initializeChannelSettingsVo);
        channelSettingsRepository.save(channelSettings);
    }

    public void remove(String passiveId, String channelSettingsId) {
        log.info("Remove channel setting has started. channelSettingsId: {}", channelSettingsId);
        ChannelSettings channelSettings = retrieveEntity(channelSettingsId);
        channelSettings.setPassiveId(passiveId);
        channelSettingsRepository.save(channelSettings);
    }

    public ChannelSettingsDto retrieve(String channelSettingsId) {
        return channelSettingsRepository.findByChannelSettingsIdAndPassiveIdIsNull(channelSettingsId)
                .map(channelSettingsDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    private ChannelSettings retrieveEntity(String channelSettingsId) {
        return channelSettingsRepository.findByChannelSettingsIdAndPassiveIdIsNull(channelSettingsId)
                .orElseThrow(NotFoundException::new);
    }
}
