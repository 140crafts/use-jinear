package co.jinear.core.controller.oauth;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.manager.oauth.GoogleOAuthCallbackManager;
import co.jinear.core.model.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/oauth/google/callback")
public class GoogleOAuthCallbackController {

    private final FeProperties feProperties;
    private final GoogleOAuthCallbackManager googleOAuthCallbackManager;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestParam String code, @RequestParam String scope, HttpServletResponse response) throws IOException {
        googleOAuthCallbackManager.login(code, scope, response);
        response.sendRedirect(feProperties.getHomeUrl());
    }

    @GetMapping("/attach-mail")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse attachMail(@RequestParam String code, @RequestParam String scope, @RequestParam String state) {
        return googleOAuthCallbackManager.attachMail(code, scope, state);
    }

    @GetMapping("/attach-calendar")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse attachCalendar(@RequestParam String code, @RequestParam String scope) {
        return googleOAuthCallbackManager.attachCalendar(code, scope);
    }
}
