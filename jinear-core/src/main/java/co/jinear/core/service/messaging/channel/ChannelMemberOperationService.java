package co.jinear.core.service.messaging.channel;

import co.jinear.core.model.entity.messaging.ChannelMember;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import co.jinear.core.repository.messaging.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberOperationService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelMemberRetrieveService channelMemberRetrieveService;

    public void addMember(String channelId, String accountId, ChannelMemberRoleType roleType) {
        log.info("Add member to channel has started. channelId: {}, accountId: {}", channelId, accountId);
        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannelId(channelId);
        channelMember.setAccountId(accountId);
        channelMember.setRoleType(roleType);
        channelMemberRepository.save(channelMember);
    }

    public void removeMember(String passiveId, String channelId, String accountId) {
        log.info("Remove channel member has started. channelId: {}, accountId: {}, passiveId: {}", channelId, accountId, passiveId);
        ChannelMember channelMember = channelMemberRetrieveService.retrieveEntity(channelId, accountId);
        channelMember.setPassiveId(passiveId);
        channelMemberRepository.save(channelMember);
    }

    public void updateRole(String channelMemberId, ChannelMemberRoleType roleType) {
        log.info("Update role has started. channelMemberId: {}, roleType: {}", channelMemberId, roleType);
        ChannelMember channelMember = channelMemberRetrieveService.retrieveEntity(channelMemberId);
        channelMember.setRoleType(roleType);
        channelMemberRepository.save(channelMember);
    }

    public boolean checkIsMember(String channelId, String accountId) {
        log.info("Check is member has started. channelId: {}, accountId: {}", channelId, accountId);
        return channelMemberRepository.existsByChannelIdAndAccountIdAndPassiveIdIsNull(channelId, accountId);
    }
}
