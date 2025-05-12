package co.jinear.core.controller.notification;

import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.model.response.notification.RetrieveUnreadNotificationEventCountResponse;
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

    @GetMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationEventListingResponse retrieveNotifications(@PathVariable String workspaceId,
                                                                  @RequestParam(required = false, defaultValue = "0") Integer page) {
        return notificationEventListingManager.retrieveNotifications(workspaceId, page);
    }

    @GetMapping("/{workspaceId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationEventListingResponse retrieveTeamNotifications(@PathVariable String workspaceId,
                                                                      @PathVariable String teamId,
                                                                      @RequestParam(required = false, defaultValue = "0") Integer page) {
        return notificationEventListingManager.retrieveTeamNotifications(workspaceId, teamId, page);
    }

    @GetMapping("/{workspaceId}/unread-count")
    @ResponseStatus(HttpStatus.OK)
    public RetrieveUnreadNotificationEventCountResponse retrieveUnreadNotificationCount(@PathVariable String workspaceId) {
        return notificationEventListingManager.retrieveUnreadNotificationCount(workspaceId);
    }
}
