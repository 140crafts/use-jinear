package co.jinear.core.manager.messaging;

import co.jinear.core.model.dto.messaging.token.MessagingTokenDto;
import co.jinear.core.model.response.messaging.MessagingTokenResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.token.MessagingTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingTokenManager {

    private final MessagingTokenService messagingTokenService;
    private final SessionInfoService sessionInfoService;

    public MessagingTokenResponse retrieveToken() {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve messaging token has started. currentAccountId: {}", currentAccountId);
        MessagingTokenDto messagingTokenDto = messagingTokenService.initialize(currentAccountId);
        return mapResponse(messagingTokenDto);
    }

    private MessagingTokenResponse mapResponse(MessagingTokenDto messagingTokenDto) {
        MessagingTokenResponse messagingTokenResponse = new MessagingTokenResponse();
        messagingTokenResponse.setMessagingTokenDto(messagingTokenDto);
        return messagingTokenResponse;
    }
}
