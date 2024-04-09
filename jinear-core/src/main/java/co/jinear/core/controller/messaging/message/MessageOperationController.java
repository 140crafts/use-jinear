package co.jinear.core.controller.messaging.message;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/message/operation")
public class MessageOperationController {

    // /thread/{threadId}/send
    // /conversation/{conversation}/send
}
