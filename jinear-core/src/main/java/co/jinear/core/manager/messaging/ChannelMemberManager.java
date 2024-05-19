package co.jinear.core.manager.messaging;

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
import co.jinear.core.service.messaging.channel.ChannelMemberRetrieveService;
import co.jinear.core.service.messaging.channel.ChannelRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberManager {

    private final SessionInfoService sessionInfoService;
    private final ChannelMemberListingService channelMemberListingService;
    private final ChannelMemberOperationService channelMemberOperationService;
    private final ChannelMemberRetrieveService channelMemberRetrieveService;
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
        channelAccessValidator.validateChannelAccess(channelId, currentAccountId);
        log.info("Retrieve member list has started. channelId: {}, currentAccountId: {}", channelId, currentAccountId);
        List<ChannelMemberDto> channelMemberDtos = channelMemberListingService.retrieveChannelMembers(channelId);
        return mapResponse(channelMemberDtos);
    }

    public BaseResponse join(String channelId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        channelAccessValidator.validateIsNotAlreadyMember(channelId, currentAccountId);
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
        channelAccessValidator.validateChannelHasAnotherAdmin(channelId);
        channelMemberOperationService.removeMember(passiveId, channelId, currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse add(String channelId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateUserNotTryingToCallItself(accountId, currentAccountId);
        channelAccessValidator.validateIsNotAlreadyMember(channelId, accountId);
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

    public BaseResponse mute(String channelId, ZonedDateTime silentUntil) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChannelMemberDto channelMemberDto = channelMemberRetrieveService.retrieve(channelId, currentAccountId);
        log.info("Mute channel has started. currentAccountId: {}, channelId: {}, silentUntil: {}", currentAccountId, channelId, silentUntil);
        channelMemberOperationService.updateSilentUntil(channelMemberDto.getChannelMemberId(), silentUntil);
        return new BaseResponse();
    }

    public BaseResponse authorize(String channelId, String toAccountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateUserNotTryingToCallItself(toAccountId, currentAccountId);
        channelAccessValidator.validateChannelAdminAccess(currentAccountId, channelId);
        log.info("Authorize account as admin has started. channelId: {}, toAccountId: {}, currentAccountId: {}", channelId, toAccountId, currentAccountId);
        ChannelMemberDto channelMemberDto = channelMemberRetrieveService.retrieve(channelId, toAccountId);
        channelMemberOperationService.updateRole(channelMemberDto.getChannelMemberId(), ChannelMemberRoleType.ADMIN);
        return new BaseResponse();
    }

    public BaseResponse unAuthorize(String channelId, String toAccountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateUserNotTryingToCallItself(toAccountId, currentAccountId);
        channelAccessValidator.validateChannelAdminAccess(currentAccountId, channelId);
        log.info("UnAuthorize account as admin has started. channelId: {}, toAccountId: {}, currentAccountId: {}", channelId, toAccountId, currentAccountId);
        ChannelMemberDto channelMemberDto = channelMemberRetrieveService.retrieve(channelId, toAccountId);
        channelMemberOperationService.updateRole(channelMemberDto.getChannelMemberId(), ChannelMemberRoleType.MEMBER);
        return new BaseResponse();
    }

    private void validateChannelIsAccessible(String channelId) {
        PlainChannelDto plainChannelDto = channelRetrieveService.retrievePlain(channelId);
        ChannelVisibilityType channelVisibilityType = plainChannelDto.getChannelVisibilityType();
        if (ChannelVisibilityType.MEMBERS_ONLY.equals(channelVisibilityType)) {
            throw new NoAccessException();
        }
    }

    private void validateUserNotTryingToCallItself(String accountId, String currentAccountId) {
        if (currentAccountId.equalsIgnoreCase(accountId)) {
            throw new NoAccessException();
        }
    }

    private ChannelMemberListingResponse mapResponse(List<ChannelMemberDto> channelMemberDtos) {
        ChannelMemberListingResponse channelMemberListingResponse = new ChannelMemberListingResponse();
        channelMemberListingResponse.setMemberList(channelMemberDtos);
        return channelMemberListingResponse;
    }
}
