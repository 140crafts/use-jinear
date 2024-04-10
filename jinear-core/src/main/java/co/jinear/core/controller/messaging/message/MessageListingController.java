package co.jinear.core.controller.messaging.message;

import co.jinear.core.manager.messaging.MessageListingManager;
import co.jinear.core.model.response.messaging.MessageListingPaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/message")
public class MessageListingController {

    private final MessageListingManager messageListingManager;

    @GetMapping("/thread/{threadId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageListingPaginatedResponse retrieve(@PathVariable String threadId,
                                                    @Valid @RequestParam("before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime before) {
        return messageListingManager.listThreadMessagesBefore(threadId, before);
    }
}
