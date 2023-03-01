package co.jinear.core.controller;

import co.jinear.core.service.account.AccountRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final AccountRoleService accountRoleService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZonedDateTime userTime = zonedDateTime.withZoneSameInstant(ZoneId.of("GMT+04:00"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("(HH:mm dd.MM.yyyy)");
        String formattedString = userTime.format(formatter);
        System.out.println(formattedString);

//        accountRoleService.assignRoleToAccount("01gt2dqc3b3f9rycxmhyb338zs", SERVICE);
        System.out.println(httpEntity.getBody());
    }
}
