package co.jinear.core.service.messaging.token;

import co.jinear.core.model.dto.messaging.token.MessagingTokenDto;
import co.jinear.core.system.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingTokenService {

    private final JwtHelper jwtHelper;

    public MessagingTokenDto initialize(String accountId) {
        log.info("Initialize messaging token has started. accountId: {}", accountId);
        String token = jwtHelper.generateMessagingToken(accountId);
        return new MessagingTokenDto(token);
    }
}
