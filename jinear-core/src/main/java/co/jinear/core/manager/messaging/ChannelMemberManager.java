package co.jinear.core.manager.messaging;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ChannelMemberListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.channel.ChannelMemberListingService;
import co.jinear.core.service.messaging.channel.ChannelMemberOperationService;
import co.jinear.core.service.messaging.channel.ChannelRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberManager {

    private final SessionInfoService sessionInfoService;
    private final ChannelMemberListingService channelMemberListingService;
    private final ChannelMemberOperationService channelMemberOperationService;
    private final ChannelRetrieveService channelRetrieveService;
    private final ChannelAccessValidator channelAccessValidator;
    private final PassiveService passiveService;

    public ChannelMemberListingResponse retrieveMemberships(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve memberships has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        List<ChannelMemberDto> channelMemberDtos = channelMemberListingService.retrieveAccountChannelMemberships(workspaceId, currentAccountId);
        return mapResponse(channelMemberDtos);
    }

    public ChannelMemberListingResponse retrieveMemberList(String channelId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateChannelMember(channelId, currentAccountId);
        log.info("Retrieve member list has started. channelId: {}, currentAccountId: {}", channelId, currentAccountId);
        List<ChannelMemberDto> channelMemberDtos = channelMemberListingService.retrieveChannelMembers(channelId);
        return mapResponse(channelMemberDtos);
    }

    public BaseResponse join(String channelId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateIsNotAlreadyMember(channelId, currentAccountId);
        validateChannelIsAccessible(channelId);
        log.info("Join has started. channelId: {}", channelId);
        channelMemberOperationService.addMember(channelId, currentAccountId, ChannelMemberRoleType.MEMBER);
        return new BaseResponse();
    }

    @Transactional
    public BaseResponse leave(String channelId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Leave has started. channelId: {}, currentAccountId: {}", channelId, currentAccountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        channelMemberOperationService.removeMember(passiveId, channelId, currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse add(String channelId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateIsNotAlreadyMember(channelId, currentAccountId);
        validateUserNotTryingToCallItself(accountId, currentAccountId);
        log.info("Add has started. currentAccountId: {}, channelId: {}, accountId: {}", currentAccountId, channelId, accountId);
        channelAccessValidator.validateChannelAdminAccess(currentAccountId, channelId);
        channelMemberOperationService.addMember(channelId, accountId, ChannelMemberRoleType.MEMBER);
        return new BaseResponse();
    }

    @Transactional
    public BaseResponse remove(String channelId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateUserNotTryingToCallItself(accountId, currentAccountId);
        channelAccessValidator.validateChannelAdminAccess(currentAccountId, channelId);
        log.info("Remove has started. currentAccountId: {}, channelId: {}, accountId: {}", currentAccountId, channelId, accountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        channelMemberOperationService.removeMember(passiveId, channelId, accountId);
        return new BaseResponse();
    }

    private void validateChannelIsAccessible(String channelId) {
        PlainChannelDto plainChannelDto = channelRetrieveService.retrievePlain(channelId);
        ChannelVisibilityType channelVisibilityType = plainChannelDto.getChannelVisibilityType();
        if (ChannelVisibilityType.MEMBERS_ONLY.equals(channelVisibilityType)) {
            throw new NoAccessException();
        }
    }

    private void validateIsNotAlreadyMember(String channelId, String currentAccountId) {
        if (channelMemberOperationService.checkIsMember(channelId, currentAccountId)) {
            throw new BusinessException();
        }
    }

    private void validateChannelMember(String channelId, String currentAccountId) {
        if (Boolean.FALSE.equals(channelMemberOperationService.checkIsMember(channelId, currentAccountId))) {
            throw new NoAccessException();
        }
    }

    private void validateUserNotTryingToCallItself(String accountId, String currentAccountId) {
        if (currentAccountId.equalsIgnoreCase(accountId)) {
            throw new BusinessException();
        }
    }

    private ChannelMemberListingResponse mapResponse(List<ChannelMemberDto> channelMemberDtos) {
        ChannelMemberListingResponse channelMemberListingResponse = new ChannelMemberListingResponse();
        channelMemberListingResponse.setMemberList(channelMemberDtos);
        return channelMemberListingResponse;
    }
}
