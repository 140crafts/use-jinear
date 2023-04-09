package co.jinear.core.controller.notification;

import co.jinear.core.manager.notification.NotificationTargetManager;
import co.jinear.core.model.request.notification.NotificationTargetInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/notification/target")
public class NotificationTargetController {

    private final NotificationTargetManager notificationTargetManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeNotificationTarget(@Valid @RequestBody NotificationTargetInitializeRequest notificationTargetInitializeRequest) {
        return notificationTargetManager.initializeNotificationTarget(notificationTargetInitializeRequest);
    }
}
