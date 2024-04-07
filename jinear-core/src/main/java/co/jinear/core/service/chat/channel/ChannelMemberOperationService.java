package co.jinear.core.service.chat.channel;

import co.jinear.core.model.entity.chat.ChannelMember;
import co.jinear.core.repository.chat.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberOperationService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelMemberRetrieveService channelMemberRetrieveService;

    public void addMember(String channelId, String accountId) {
        log.info("Add member to channel has started. channelId: {}, accountId: {}", channelId, accountId);
        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannelId(channelId);
        channelMember.setAccountId(accountId);
        channelMemberRepository.save(channelMember);
    }

    public void removeMember(String passiveId, String channelMemberId) {
        log.info("Remove channel member has started. channelMemberId: {}, passiveId: {}", channelMemberId, passiveId);
        ChannelMember channelMember = channelMemberRetrieveService.retrieveEntity(channelMemberId);
        channelMember.setPassiveId(passiveId);
    }
}
