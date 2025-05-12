package co.jinear.core.service.messaging.channel;

import co.jinear.core.model.entity.messaging.ChannelMember;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import co.jinear.core.repository.messaging.ChannelMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

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

    public void addRobot(String channelId, String robotId, ChannelMemberRoleType roleType) {
        log.info("Add robot to channel has started. channelId: {}, robotId: {}", channelId, robotId);
        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannelId(channelId);
        channelMember.setRobotId(robotId);
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

    public boolean checkIsRobotMember(String channelId, String robotId) {
        log.info("Check is robot member has started. channelId: {}, robotId: {}", channelId, robotId);
        return channelMemberRepository.existsByChannelIdAndRobotIdAndPassiveIdIsNull(channelId, robotId);
    }

    public void updateSilentUntil(String channelMemberId, ZonedDateTime silentUntil) {
        log.info("Update silent until has started. channelMemberId: {}, silentUntil: {}", channelMemberId, silentUntil);
        ChannelMember channelMember = channelMemberRetrieveService.retrieveEntity(channelMemberId);
        channelMember.setSilentUntil(silentUntil);
        channelMemberRepository.save(channelMember);
    }

    @Transactional
    public void updateLastCheck(String channelId, String accountId, ZonedDateTime lastCheck) {
        log.info("Update last check has started. channelId: {}, accountId: {}, lastCheck: {}", channelId, accountId, lastCheck);
        channelMemberRepository.updateLastCheck(channelId, accountId, lastCheck);
    }
}
