package co.jinear.core.validator.messaging.channel;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.service.messaging.channel.ChannelMemberOperationService;
import co.jinear.core.service.messaging.channel.ChannelMemberRetrieveService;
import co.jinear.core.service.messaging.channel.ChannelRetrieveService;
import co.jinear.core.system.NumberCompareHelper;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelAccessValidator {

    private final ChannelRetrieveService channelRetrieveService;
    private final ChannelMemberRetrieveService channelMemberRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final ChannelMemberOperationService channelMemberOperationService;

    public void validateChannelAccess(String channelId, String currentAccountId) {
        if (Boolean.FALSE.equals(channelMemberOperationService.checkIsMember(channelId, currentAccountId))) {
            throw new NoAccessException();
        }
    }

    public void validateRobotChannelAccess(String channelId, String robotId) {
        if (Boolean.FALSE.equals(channelMemberOperationService.checkIsRobotMember(channelId, robotId))) {
            throw new NoAccessException();
        }
    }

    public void validateIsNotAlreadyMember(String channelId, String currentAccountId) {
        if (channelMemberOperationService.checkIsMember(channelId, currentAccountId)) {
            throw new BusinessException();
        }
    }

    public void validateRobotIsNotAlreadyMember(String channelId, String robotId) {
        if (channelMemberOperationService.checkIsRobotMember(channelId, robotId)) {
            throw new BusinessException();
        }
    }

    public void validateChannelParticipationAccess(String accountId, String channelId) {
        log.info("Validate channel new thread access has started. accountId: {}, channelId: {}", accountId, channelId);
        PlainChannelDto plainChannelDto = channelRetrieveService.retrievePlain(channelId);
        ChannelParticipationType participationType = plainChannelDto.getParticipationType();
        String workspaceId = plainChannelDto.getWorkspaceId();
        switch (participationType) {
            case EVERYONE -> validateChannelAccess(channelId, accountId);
            case ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY -> validateChannelAdminAccess(accountId, channelId);
            case READ_ONLY ->
                    workspaceValidator.validateWorkspaceRoles(accountId, workspaceId, List.of(WorkspaceAccountRoleType.OWNER, WorkspaceAccountRoleType.ADMIN));
        }
    }

    public void validateRobotChannelParticipationAccess(String robotId, String channelId) {
        log.info("Validate channel new thread access has started. robotId: {}, channelId: {}", robotId, channelId);
        PlainChannelDto plainChannelDto = channelRetrieveService.retrievePlain(channelId);
        ChannelParticipationType participationType = plainChannelDto.getParticipationType();
        switch (participationType) {
            case EVERYONE -> validateRobotChannelAccess(channelId, robotId);
            case ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY ->
                    validateRobotChannelAdminAccess(robotId, channelId);
            case READ_ONLY -> throw new NoAccessException();
        }
    }

    public void validateChannelAdminAccess(String accountId, String channelId) {
        log.info("Validate channel admin access has started. accountId: {}, channelId: {}", accountId, channelId);
        PlainChannelDto plainChannelDto = channelRetrieveService.retrievePlain(channelId);
        boolean accountHasChannelAdminAccess = channelMemberRetrieveService.doesAccountHaveChannelAdminAccess(accountId, channelId);
        boolean accountHasWorkspaceAdminAccess = workspaceValidator.isWorkspaceAdminOrOwner(accountId, plainChannelDto.getWorkspaceId());
        if (!accountHasChannelAdminAccess && !accountHasWorkspaceAdminAccess) {
            throw new NoAccessException();
        }
    }

    public void validateRobotChannelAdminAccess(String robotId, String channelId) {
        log.info("Validate channel admin access has started. robotId: {}, channelId: {}", robotId, channelId);
        boolean robotHasChannelAdminAccess = channelMemberRetrieveService.doesRobotHaveChannelAdminAccess(robotId, channelId);
        if (!robotHasChannelAdminAccess) {
            throw new NoAccessException();
        }
    }

    public void validateChannelHasAnotherAdmin(String channelId) {
        Long currentAdminCount = channelMemberRetrieveService.retrieveChannelAdminAccessMemberCount(channelId);
        if (NumberCompareHelper.isLessThanOrEqual(currentAdminCount, 1L)) {
            throw new BusinessException("messaging.channel.you-are-only-admin");
        }
    }
}
