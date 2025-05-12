package co.jinear.core.controller.messaging.thread;

import co.jinear.core.manager.messaging.RobotsThreadManager;
import co.jinear.core.model.request.messaging.thread.RobotsInitializeThreadRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/robots/messaging/thread")
public class ThreadRobotsController {

    private final RobotsThreadManager robotsThreadManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeThread(@RequestHeader("X-CHANNEL-ID") String channelId,
                                         @Valid @RequestBody RobotsInitializeThreadRequest initializeThreadRequest) {
        return robotsThreadManager.initializeThread(channelId, initializeThreadRequest);
    }
}
