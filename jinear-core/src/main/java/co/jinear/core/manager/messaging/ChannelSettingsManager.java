package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.channel.InitializeChannelSettingsRequestConverter;
import co.jinear.core.model.dto.messaging.channel.ChannelSettingsDto;
import co.jinear.core.model.request.messaging.channel.InitializeChannelSettingsRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.messaging.channel.InitializeChannelSettingsVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.channel.ChannelSettingsService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.messaging.channel.ChannelAccessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelSettingsManager {

    private final ChannelSettingsService channelSettingsService;
    private final SessionInfoService sessionInfoService;
    private final InitializeChannelSettingsRequestConverter initializeChannelSettingsRequestConverter;
    private final ChannelAccessValidator channelAccessValidator;
    private final PassiveService passiveService;

    public BaseResponse initialize(InitializeChannelSettingsRequest initializeChannelSettingsRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String channelId = initializeChannelSettingsRequest.getChannelId();
        channelAccessValidator.validateChannelAdminAccess(currentAccountId, channelId);

        InitializeChannelSettingsVo initializeChannelSettingsVo = initializeChannelSettingsRequestConverter.convert(initializeChannelSettingsRequest);
        channelSettingsService.initialize(channelId, initializeChannelSettingsVo);
        return new BaseResponse();
    }

    @Transactional
    public BaseResponse remove(String channelSettingsId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChannelSettingsDto channelSettingsDto = channelSettingsService.retrieve(channelSettingsId);
        channelAccessValidator.validateChannelAdminAccess(currentAccountId, channelSettingsDto.getChannelId());

        log.info("Remove has started. channelSettingsId: {}", channelSettingsId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        channelSettingsService.remove(passiveId, channelSettingsId);
        return new BaseResponse();
    }
}
