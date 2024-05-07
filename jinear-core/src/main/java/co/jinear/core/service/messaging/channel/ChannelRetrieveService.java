package co.jinear.core.service.messaging.channel;

import co.jinear.core.converter.messaging.channel.ChannelDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.channel.ChannelDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.entity.messaging.Channel;
import co.jinear.core.repository.messaging.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelRetrieveService {

    private final ChannelRepository channelRepository;
    private final ChannelDtoConverter channelDtoConverter;

    public Channel retrieveEntity(String channelId) {
        log.info("Retrieve channel entity has started. channelId: {}", channelId);
        return channelRepository.findByChannelIdAndPassiveIdIsNull(channelId)
                .orElseThrow(NotFoundException::new);
    }

    public ChannelDto retrieve(String channelId) {
        log.info("Retrieve channel has started. channelId: {}", channelId);
        return channelRepository.findByChannelIdAndPassiveIdIsNull(channelId)
                .map(channelDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public PlainChannelDto retrievePlain(String channelId) {
        log.info("Retrieve plain channel has started. channelId: {}", channelId);
        return channelRepository.findByChannelIdAndPassiveIdIsNull(channelId)
                .map(channelDtoConverter::convertPlain)
                .orElseThrow(NotFoundException::new);
    }
}
