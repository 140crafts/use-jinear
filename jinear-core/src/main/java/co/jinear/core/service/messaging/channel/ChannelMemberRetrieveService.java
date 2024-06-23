package co.jinear.core.service.messaging.channel;

import co.jinear.core.converter.messaging.channel.ChannelMemberDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.model.entity.messaging.ChannelMember;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import co.jinear.core.repository.messaging.ChannelMemberRepository;
import co.jinear.core.system.NumberCompareHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberRetrieveService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelMemberDtoConverter channelMemberDtoConverter;

    public ChannelMember retrieveEntity(String channelMemberId) {
        log.info("Retrieve entity has started. channelMemberId: {}", channelMemberId);
        return channelMemberRepository.findByChannelMemberIdAndPassiveIdIsNull(channelMemberId)
                .orElseThrow(NotFoundException::new);
    }

    public ChannelMember retrieveEntity(String channelId, String accountId) {
        log.info("Retrieve entity has started. channelId: {}, accountId: {}", channelId, accountId);
        return channelMemberRepository.findByChannelIdAndAccountIdAndPassiveIdIsNull(channelId, accountId)
                .orElseThrow(NotFoundException::new);
    }

    public ChannelMemberDto retrieve(String channelId, String accountId) {
        log.info("Retrieve has started. channelId: {}, accountId: {}", channelId, accountId);
        return channelMemberRepository.findByChannelIdAndAccountIdAndPassiveIdIsNull(channelId, accountId)
                .map(channelMemberDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public boolean doesAccountHaveChannelAdminAccess(String accountId, String channelId) {
        log.info("Does account have channel admin access started. accountId: {}, channelId: {}", accountId, channelId);
        Long count = channelMemberRepository.countAllByChannelIdAndAccountIdAndRoleTypeIsInAndPassiveIdIsNull(channelId, accountId, List.of(ChannelMemberRoleType.ADMIN, ChannelMemberRoleType.OWNER));
        return !NumberCompareHelper.isEquals(count, 0);
    }

    public boolean doesRobotHaveChannelAdminAccess(String robotId, String channelId) {
        log.info("Does robot have channel admin access started. robotId: {}, channelId: {}", robotId, channelId);
        Long count = channelMemberRepository.countAllByChannelIdAndRobotIdAndRoleTypeIsInAndPassiveIdIsNull(channelId, robotId, List.of(ChannelMemberRoleType.ADMIN, ChannelMemberRoleType.OWNER));
        return !NumberCompareHelper.isEquals(count, 0);
    }

    public Long retrieveChannelAdminAccessMemberCount(String channelId) {
        log.info("Retrieve channel admin access member count has started. channelId: {}", channelId);
        return channelMemberRepository.countAllByChannelIdAndRoleTypeIsInAndPassiveIdIsNullAndAccount_PassiveIdIsNullAndAccount_Ghost(channelId, List.of(ChannelMemberRoleType.ADMIN, ChannelMemberRoleType.OWNER), Boolean.FALSE);
    }
}
