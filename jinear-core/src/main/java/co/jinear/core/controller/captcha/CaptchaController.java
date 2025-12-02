package co.jinear.core.controller.captcha;

import co.jinear.core.manager.captcha.CaptchaManager;
import co.jinear.core.model.response.captcha.CaptchaChallengeResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/captcha")
public class CaptchaController {

    private final CaptchaManager captchaManager;

    @GetMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public CaptchaChallengeResponse generateCaptcha(HttpServletRequest httpServletRequest) {
        return captchaManager.generate(httpServletRequest);
    }
}
