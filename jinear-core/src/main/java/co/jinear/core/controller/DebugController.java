package co.jinear.core.controller;

import co.jinear.core.service.mail.LocaleStringService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusService;
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

    private final TeamWorkflowStatusService teamWorkflowStatusService;
    private final LocaleStringService localeStringService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {

        System.out.println(httpEntity.getBody());
    }
}
