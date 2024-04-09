package co.jinear.core.service.messaging.channel;

import co.jinear.core.converter.messaging.channel.ChannelMemberDtoConverter;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.repository.messaging.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberListingService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelMemberDtoConverter channelMemberDtoConverter;

    public List<ChannelMemberDto> retrieveAccountChannelMemberships(String workspaceId, String accountId) {
        log.info("Retrieve account channel memberships has started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        return channelMemberRepository.findAllByChannel_WorkspaceIdAndAccountIdAndPassiveIdIsNull(workspaceId, accountId)
                .stream()
                .map(channelMemberDtoConverter::convert)
                .toList();
    }

    public List<ChannelMemberDto> retrieveChannelMembers(String channelId) {
        log.info("Retrieve channel members has started. channelId: {}", channelId);
        return channelMemberRepository.findAllByChannelIdAndPassiveIdIsNull(channelId)
                .stream()
                .map(channelMemberDtoConverter::convert)
                .toList();
    }
}
