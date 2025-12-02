package co.jinear.core.manager.captcha;

import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;
import co.jinear.core.model.response.captcha.CaptchaChallengeResponse;
import co.jinear.core.service.captcha.CaptchaChallengeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaManager {

    private final CaptchaChallengeService captchaChallengeService;

    public CaptchaChallengeResponse generate(HttpServletRequest httpServletRequest) {
        log.info("Generate captcha has started.");
        CaptchaChallengeDto captchaChallengeDto = captchaChallengeService.initialize(httpServletRequest);
        return mapResponse(captchaChallengeDto);
    }

    private CaptchaChallengeResponse mapResponse(CaptchaChallengeDto captchaChallengeDto) {
        CaptchaChallengeResponse captchaChallengeResponse = new CaptchaChallengeResponse();
        captchaChallengeResponse.setCaptchaChallengeDto(captchaChallengeDto);
        return captchaChallengeResponse;
    }
}
