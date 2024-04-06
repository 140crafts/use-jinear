package co.jinear.core.service.chat.channel;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.chat.Channel;
import co.jinear.core.repository.chat.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelRetrieveService {

    private final ChannelRepository channelRepository;

    public Channel retrieveEntity(String channelId) {
        log.info("Retrieve channel entity has started. channelId: {}", channelId);
        return channelRepository.findByChannelIdAndPassiveIdIsNull(channelId)
                .orElseThrow(NotFoundException::new);
    }
}
