package co.jinear.core.controller.messaging.message;

import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.response.messaging.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/robots/messaging/message/operation")
public class RobotMessageOperationController {

    @PostMapping("/thread/{threadId}/send")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse sendToThread(@PathVariable String threadId,
                                        @Valid @RequestBody SendMessageRequest sendMessageRequest) {

        return new MessageResponse();
    }

}
