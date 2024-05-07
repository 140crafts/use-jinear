package co.jinear.core.controller.messaging.token;

import co.jinear.core.manager.messaging.MessagingTokenManager;
import co.jinear.core.model.response.messaging.MessagingTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/token")
public class MessagingTokenController {

    private final MessagingTokenManager messagingTokenManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MessagingTokenResponse retrieveSocketToken() {
        return messagingTokenManager.retrieveToken();
    }

}
