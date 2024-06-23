package co.jinear.core.controller.messaging.message;

import co.jinear.core.manager.messaging.RobotsMessageOperationManager;
import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/robots/messaging/message/operation")
public class RobotMessageOperationController {

    private final RobotsMessageOperationManager robotsMessageOperationManager;

    @PostMapping("/thread")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse sendToThread(@RequestHeader("X-THREAD-ID") String threadId,
                                     @Valid @RequestBody SendMessageRequest sendMessageRequest) {
        return robotsMessageOperationManager.sendToThread(threadId, sendMessageRequest);
    }
}
