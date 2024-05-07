package co.jinear.core.service.messaging.channel;

import co.jinear.core.converter.messaging.channel.ChannelDtoConverter;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.repository.messaging.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelListingService {

    private final ChannelRepository channelRepository;
    private final ChannelDtoConverter channelDtoConverter;

    public List<PlainChannelDto> listChannels(String workspaceId, List<ChannelVisibilityType> channelVisibilityTypeList) {
        log.info("List channels has started. workspaceId: {}, channelVisibilityTypeList: {}", workspaceId, StringUtils.join(channelVisibilityTypeList, ','));
        return channelRepository.findAllByWorkspaceIdAndChannelVisibilityTypeIsInAndPassiveIdIsNull(workspaceId, channelVisibilityTypeList)
                .stream()
                .map(channelDtoConverter::convertPlain)
                .toList();
    }
}
