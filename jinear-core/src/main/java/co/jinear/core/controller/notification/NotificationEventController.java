package co.jinear.core.controller.notification;

import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/notification/event")
public class NotificationEventController {

    private final NotificationEventListingManager notificationEventListingManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public NotificationEventListingResponse retrieveNotifications(@RequestParam(required = false, defaultValue = "0") Integer page) {
        return notificationEventListingManager.retrieveNotifications(page);
    }
}
