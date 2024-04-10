package co.jinear.core.controller.messaging.thread;

import co.jinear.core.manager.messaging.ThreadManager;
import co.jinear.core.model.request.messaging.thread.InitializeThreadRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/thread")
public class ThreadController {

    private final ThreadManager threadManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeThread(@Valid @RequestBody InitializeThreadRequest initializeThreadRequest) {
        return threadManager.initializeThread(initializeThreadRequest);
    }

}
