package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.channel.InitializeChannelRequestConverter;
import co.jinear.core.converter.messaging.channel.WorkspaceAccountRoleTypeChannelVisibilityListMapper;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.model.dto.messaging.channel.ChannelMembershipInfoDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.model.request.messaging.channel.InitializeChannelRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ChannelMembershipInfoListingResponse;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.channel.ChannelListingService;
import co.jinear.core.service.messaging.channel.ChannelMemberListingService;
import co.jinear.core.service.messaging.channel.ChannelOperationService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final ChannelOperationService channelOperationService;
    private final InitializeChannelRequestConverter initializeChannelRequestConverter;
    private final ChannelAccessValidator channelAccessValidator;
    private final ChannelListingService channelListingService;
    private final WorkspaceAccountRoleTypeChannelVisibilityListMapper workspaceAccountRoleTypeChannelVisibilityListMapper;
    private final ChannelMemberListingService channelMemberListingService;

    public BaseResponse initializeChannel(InitializeChannelRequest initializeChannelRequest) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, initializeChannelRequest.getWorkspaceId());
        log.info("Initialize channel has started. accountId: {}", accountId);
        InitializeChannelVo initializeChannelVo = initializeChannelRequestConverter.convert(initializeChannelRequest, accountId);
        channelOperationService.initialize(initializeChannelVo);
        return new BaseResponse();
    }

    public BaseResponse updateParticipation(String channelId, ChannelParticipationType participationType) {
        String accountId = sessionInfoService.currentAccountId();
        channelAccessValidator.validateChannelAdminAccess(accountId, channelId);
        log.info("Update participation has started. accountId: {}", accountId);
        channelOperationService.updateParticipation(channelId, participationType);
        return new BaseResponse();
    }

    public BaseResponse updateVisibility(String channelId, ChannelVisibilityType channelVisibilityType) {
        String accountId = sessionInfoService.currentAccountId();
        channelAccessValidator.validateChannelAdminAccess(accountId, channelId);
        log.info("Update visibility has started. accountId: {}", accountId);
        channelOperationService.updateVisibility(channelId, channelVisibilityType);
        return new BaseResponse();
    }

    public ChannelMembershipInfoListingResponse listChannels(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        log.info("List channels has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        List<ChannelVisibilityType> channelVisibilityTypes = workspaceAccountRoleTypeChannelVisibilityListMapper.map(workspaceId, accountId);
        List<PlainChannelDto> channelDtoList = channelListingService.listChannels(workspaceId, channelVisibilityTypes);

        List<ChannelMemberDto> memberships = channelMemberListingService.retrieveAccountChannelMemberships(workspaceId, accountId);
        return mapResponse(channelDtoList, memberships);
    }

    private ChannelMembershipInfoListingResponse mapResponse(List<PlainChannelDto> channelDtoList, List<ChannelMemberDto> memberships) {
        Map<String, ChannelMemberDto> membershipsMap = memberships.stream().collect(Collectors.toMap(ChannelMemberDto::getChannelId, Function.identity()));
        ChannelMembershipInfoListingResponse channelMembershipInfoListingResponse = new ChannelMembershipInfoListingResponse();
        List<ChannelMembershipInfoDto> channelMembershipInfoDtoList = channelDtoList
                .stream()
                .map(plainChannelDto -> new ChannelMembershipInfoDto(plainChannelDto, membershipsMap.containsKey(plainChannelDto.getChannelId())))
                .toList();
        channelMembershipInfoListingResponse.setChannelMembershipInfoDtoList(channelMembershipInfoDtoList);
        return channelMembershipInfoListingResponse;
    }

    public BaseResponse updateTitle(String channelId, String newTitle) {
        String accountId = sessionInfoService.currentAccountId();
        channelAccessValidator.validateChannelAdminAccess(accountId, channelId);
        log.info("Update title has started. accountId: {}", accountId);
        channelOperationService.updateTitle(channelId, newTitle);
        return new BaseResponse();
    }
}
