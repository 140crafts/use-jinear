package co.jinear.core.controller.oauth;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.manager.oauth.GoogleOAuthCallbackManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/oauth/google/callback")
public class GoogleOAuthCallbackController {

    private final FeProperties feProperties;
    private final GoogleOAuthCallbackManager googleOAuthCallbackManager;

    @Validated
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestParam @Size(max = 4048) String code, @RequestParam String scope, HttpServletResponse response) throws IOException {
        googleOAuthCallbackManager.login(code, scope, response);
        response.sendRedirect(feProperties.getHomeUrl());
    }

    @Validated
    @GetMapping("/attach-mail")
    @ResponseStatus(HttpStatus.OK)
    public void attachMail(@RequestParam @Size(max = 4048) String code, @RequestParam String scope, @RequestParam String state, HttpServletResponse response) throws IOException {
        googleOAuthCallbackManager.attachMail(code, scope, state);
        response.sendRedirect(feProperties.getHomeUrl());
    }

    @Validated
    @GetMapping("/attach-calendar")
    @ResponseStatus(HttpStatus.OK)
    public void attachCalendar(@RequestParam @Size(max = 4048) String code, @RequestParam String scope, @RequestParam String state, HttpServletResponse response) throws IOException {
        googleOAuthCallbackManager.attachCalendar(code, scope, state);
        response.sendRedirect(feProperties.getHomeUrl());
    }
}
