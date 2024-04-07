package co.jinear.core.service.chat.channel;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.chat.ChannelMember;
import co.jinear.core.repository.chat.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberRetrieveService {

    private final ChannelMemberRepository channelMemberRepository;

    public ChannelMember retrieveEntity(String channelMemberId) {
        log.info("Retrieve entity has started. channelMemberId: {}", channelMemberId);
        return channelMemberRepository.findByChannelMemberIdAndPassiveIdIsNull(channelMemberId)
                .orElseThrow(NotFoundException::new);
    }
}
