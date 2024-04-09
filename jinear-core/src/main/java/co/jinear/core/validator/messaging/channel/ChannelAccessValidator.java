package co.jinear.core.validator.messaging.channel;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.service.messaging.channel.ChannelMemberRetrieveService;
import co.jinear.core.service.messaging.channel.ChannelRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelAccessValidator {

    private final ChannelRetrieveService channelRetrieveService;
    private final ChannelMemberRetrieveService channelMemberRetrieveService;
    private final WorkspaceValidator workspaceValidator;

    public void validateChannelAdminAccess(String accountId, String channelId) {
        log.info("Validate channel admin access has started. accountId: {}, channelId: {}", accountId, channelId);
        PlainChannelDto plainChannelDto = channelRetrieveService.retrievePlain(channelId);
        boolean accountHasChannelAdminAccess = channelMemberRetrieveService.doesAccountHaveChannelAdminAccess(accountId, channelId);
        boolean accountHasWorkspaceAdminAccess = workspaceValidator.isWorkspaceAdminOrOwner(accountId, plainChannelDto.getWorkspaceId());
        if (!accountHasChannelAdminAccess && !accountHasWorkspaceAdminAccess) {
            throw new NoAccessException();
        }
    }
}
