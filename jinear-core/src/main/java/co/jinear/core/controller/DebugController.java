package co.jinear.core.controller;

import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountRoleService;
import co.jinear.core.service.notification.NotificationFireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static co.jinear.core.model.enumtype.localestring.LocaleType.TR;
import static co.jinear.core.model.enumtype.notification.NotificationTemplateType.TASK_REMINDER;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final AccountRoleService accountRoleService;
    private final NotificationFireService notificationFireService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {
        NotificationSendVo notificationSendVo = new NotificationSendVo();
        notificationSendVo.setTemplateType(TASK_REMINDER);
        notificationSendVo.setLocaleType(TR);
        notificationSendVo.setAccountId("01gxjxdwmcb40yxh1n4hc3dx6p");
        notificationSendVo.setParams(Map.of("taskTag","TAG-1","taskTitle","Lorem Ipsum Sik Sok Amet Zart zurt Gomet","reminderTypeText","asdreminderTypeText reminderTypeText reminderTypeText reminderTypeText reminderTypeText"));
        notificationFireService.fire(notificationSendVo);
    }
}
