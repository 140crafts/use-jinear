package co.jinear.core.service.chat.channel;

import co.jinear.core.converter.chat.channel.ChannelEntityConverter;
import co.jinear.core.model.entity.chat.Channel;
import co.jinear.core.model.enumtype.chat.ChannelParticipationType;
import co.jinear.core.model.enumtype.chat.ChannelVisibilityType;
import co.jinear.core.model.vo.chat.channel.InitializeChannelVo;
import co.jinear.core.repository.chat.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelOperationService {

    private final ChannelRepository channelRepository;
    private final ChannelEntityConverter channelEntityConverter;
    private final ChannelSettingsService channelSettingsService;
    private final ChannelRetrieveService channelRetrieveService;

    public void initialize(InitializeChannelVo initializeChannelVo) {
        log.info("Initialize channel has started. initializeChannelVo: {}", initializeChannelVo);
        Channel channel = initializeChannel(initializeChannelVo);
        initializeInitialSettings(initializeChannelVo, channel);
    }

    public void updateParticipation(String channelId, ChannelParticipationType participationType) {
        log.info("Update participation has started. channelId: {}, participationType: {}", channelId, participationType);
        Channel channel = channelRetrieveService.retrieveEntity(channelId);
        channel.setParticipationType(participationType);
        channelRepository.save(channel);
    }

    public void updateVisibility(String channelId, ChannelVisibilityType channelVisibilityType) {
        log.info("Update visibility has started. channelId: {}, channelVisibilityType: {}", channelId, channelVisibilityType);
        Channel channel = channelRetrieveService.retrieveEntity(channelId);
        channel.setChannelVisibilityType(channelVisibilityType);
        channelRepository.save(channel);
    }

    private Channel initializeChannel(InitializeChannelVo initializeChannelVo) {
        Channel channel = channelEntityConverter.convert(initializeChannelVo);
        return channelRepository.save(channel);
    }

    private void initializeInitialSettings(InitializeChannelVo initializeChannelVo, Channel channel) {
        Optional.of(initializeChannelVo)
                .map(InitializeChannelVo::getInitialSettings)
                .map(Collection::stream)
                .ifPresent(stream -> stream.forEach(initializeChannelSettingsVo -> channelSettingsService.initialize(channel.getChannelId(), initializeChannelSettingsVo)));
    }
}
