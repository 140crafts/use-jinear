package co.jinear.core.controller;

import co.jinear.core.manager.reminder.ReminderProcessManager;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.notification.NotificationCreateService;
import co.jinear.core.service.task.subscription.TaskSubscriptionListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final AccountRoleService accountRoleService;
    private final NotificationCreateService notificationCreateService;
    private final TaskSubscriptionListingService taskSubscriptionListingService;
    private final ReminderProcessManager reminderProcessManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {

    }
}
